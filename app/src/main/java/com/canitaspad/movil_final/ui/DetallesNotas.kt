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
import com.canitaspad.movil_final.NavigationDestination
import com.canitaspad.movil_final.R
import com.canitaspad.movil_final.model.Nota
import com.canitaspad.movil_final.viewModel.NotaDetailsUiState
import com.canitaspad.movil_final.viewModel.NotaDetailsViewModel
import com.canitaspad.movil_final.viewModel.toItem
import kotlinx.coroutines.launch

object NotasDetallesDestination : NavigationDestination {
    // Ruta y título para la pantalla de detalles de la nota
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val notaIdArg = "notaId"  // Argumento de la ruta para el ID de la nota
    val routeWithArgs = "$route/{$notaIdArg}"  // Ruta con argumentos
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotaDetailsScreen(
    navigateToEditItem: (Int) -> Unit, // Función para navegar a la edición de la nota
    navigateBack: () -> Unit,  // Función para navegar hacia atrás
    modifier: Modifier = Modifier,  // Modificador para personalizar el diseño
    viewModel: NotaDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)  // ViewModel para manejar el estado de la UI
) {
    // Recolecta el estado de la UI del ViewModel
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            // Barra superior con el título y botón de retroceso
            InventoryTopAppBar(
                title = stringResource(NotasDetallesDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            // Botón flotante para editar la nota
            FloatingActionButton(
                onClick = { navigateToEditItem(uiState.value.notaDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        // Cuerpo de la pantalla de detalles de la nota
        NotaDetailsBody(
            notaDetailsUiState = uiState.value,
            onDelete = {
                // Eliminación de la nota, teniendo en cuenta la rotación de la pantalla
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
private fun NotaDetailsBody(
    notaDetailsUiState: NotaDetailsUiState,
    onDelete: () -> Unit, // Acción de eliminar
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),  // Espaciado interior
        verticalArrangement = Arrangement.spacedBy(16.dp)  // Espaciado entre los elementos
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        // Muestra los detalles de la nota
        NotaDetails(
            nota = notaDetailsUiState.notaDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )

        // Botón para eliminar la nota
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }

        // Confirmación para eliminar la nota
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun NotaDetails(
    nota: Nota, modifier: Modifier = Modifier
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
            // Muestra los detalles de la nota: título, fecha, contenido
            NotaDetailsRow(
                labelResID = R.string.titulo,
                notaDetail = nota.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            NotaDetailsRow(
                labelResID = R.string.fecha,
                notaDetail = nota.fecha,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            NotaDetailsRow(
                labelResID = R.string.contenido,
                notaDetail = nota.contenido,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }

    }
}

@Composable
private fun NotaDetailsRow(
    @StringRes labelResID: Int, notaDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID), fontWeight = FontWeight.Bold)  // Etiqueta en negrita
        Spacer(modifier = Modifier.weight(1f))  // Espaciador para separar la etiqueta del detalle
        Text(text = notaDetail)  // Muestra el detalle de la nota
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    // Diálogo de confirmación para eliminar la nota
    AlertDialog(onDismissRequest = { /* No hacer nada */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
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




