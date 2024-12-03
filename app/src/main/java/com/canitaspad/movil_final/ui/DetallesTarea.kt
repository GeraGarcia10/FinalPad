package com.canitaspad.movil_final.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canitaspad.movil_final.AppViewModelProvider
import com.canitaspad.movil_final.InventoryTopAppBar
import com.canitaspad.movil_final.R
import com.canitaspad.movil_final.model.Tarea
import com.canitaspad.movil_final.viewModel.TareaDetailsUiState
import com.canitaspad.movil_final.viewModel.TareaDetailsViewModel
import com.canitaspad.movil_final.viewModel.toItem
import kotlinx.coroutines.launch

object TareasDetallesDestination : NavigationDestination2 {
    // Ruta y título para la pantalla de detalles de la tarea
    override val route = "tarea_details"
    override val titleRes = R.string.item_detail_title2
    const val tareaIdArg = "tareaId"  // Argumento para el ID de la tarea
    val routeWithArgs = "$route/{$tareaIdArg}"  // Ruta con el ID de la tarea
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaDetailsScreen(
    navigateToEditItem: (Int) -> Unit,  // Función para navegar a la edición de la tarea
    navigateBack: () -> Unit,  // Función para navegar hacia atrás
    modifier: Modifier = Modifier,  // Modificador para personalizar el diseño
    viewModel: TareaDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)  // ViewModel que maneja el estado de la UI
) {
    // Recoge el estado de la UI del ViewModel
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            // Barra superior con el título y botón de retroceso
            InventoryTopAppBar(
                title = stringResource(TareasDetallesDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            // Botón flotante para editar la tarea
            FloatingActionButton(
                onClick = { navigateToEditItem(uiState.value.tareaDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title2),
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        // Cuerpo de la pantalla de detalles de la tarea
        TareaDetailsBody(
            tareaDetailsUiState = uiState.value,
            onComplete = { viewModel.markComplete() },  // Marcar tarea como completa
            onDelete = {
                // Eliminación de la tarea, gestionando cambios de configuración como la rotación de pantalla
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())  // Habilita desplazamiento vertical
        )
    }
}

@Composable
private fun TareaDetailsBody(
    tareaDetailsUiState: TareaDetailsUiState,
    onComplete: () -> Unit,  // Acción para marcar como completada
    onDelete: () -> Unit,    // Acción para eliminar
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),  // Espaciado interior
        verticalArrangement = Arrangement.spacedBy(16.dp)  // Espaciado entre los elementos
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        // Muestra los detalles de la tarea
        TareaDetails(
            tarea = tareaDetailsUiState.tareaDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )

        // Botón para marcar la tarea como completada
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text = stringResource(R.string.marcar))  // Texto del botón
        }

        // Botón para eliminar la tarea
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }

        // Diálogo de confirmación de eliminación
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()  // Confirma la eliminación
                },
                onDeleteCancel = { deleteConfirmationRequired = false },  // Cancela la eliminación
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TareaDetails(
    tarea: Tarea, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)  // Espaciado entre los elementos
        ) {
            // Muestra los detalles de la tarea: título, fecha, fecha de completado, si está completada, y contenido
            TareaDetailsRow(
                labelResID = R.string.titulo,
                tareaDetail = tarea.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            TareaDetailsRow(
                labelResID = R.string.fecha,
                tareaDetail = tarea.fecha,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            TareaDetailsRow(
                labelResID = R.string.fechaACompletar,
                tareaDetail = tarea.fechaACompletar,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            TareaDetailsRow(
                labelResID = R.string.completado,
                tareaDetail =
                if(tarea.isComplete){
                    stringResource(id = R.string.yes)
                } else {
                    stringResource(id = R.string.no)
                },
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            TareaDetailsRow(
                labelResID = R.string.contenido,
                tareaDetail = tarea.contenido,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Composable
private fun TareaDetailsRow(
    @StringRes labelResID: Int, tareaDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID), fontWeight = FontWeight.Bold)  // Etiqueta en negrita
        Spacer(modifier = Modifier.weight(1f))  // Espaciador para separar la etiqueta del detalle
        Text(text = tareaDetail)  // Muestra el detalle de la tarea
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    // Diálogo de confirmación para eliminar la tarea
    AlertDialog(onDismissRequest = { /* No hacer nada */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question2)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}




