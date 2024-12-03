package com.canitaspad.movil_final.ui

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canitaspad.movil_final.model.Nota
import androidx.navigation.NavHostController
import com.canitaspad.movil_final.AppTopBar
import com.canitaspad.movil_final.AppViewModelProvider
import com.canitaspad.movil_final.BarraBusqueda
import com.canitaspad.movil_final.CustomTopAppBar
import com.canitaspad.movil_final.R
import com.canitaspad.movil_final.viewModel.NotasScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasList(
    modifier: Modifier = Modifier,
    configuration: Configuration,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: NotasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    navigateToItemUpdate: (Int) -> Unit,
) {
    // Obtener el estado de la UI desde el ViewModel
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            Column {
                CustomTopAppBar(stringResource(R.string.notas)) // Barra superior personalizada
                AppTopBar(navController = navController) // Barra superior con navegación
                Spacer(modifier = Modifier.height(15.dp))

                // Mostrar la barra de búsqueda en modo landscape y portrait
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // Barra de búsqueda en modo Landscape con FAB
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BarraBusqueda(
                            label = R.string.busqueda,
                            leadingIcon = R.drawable.lupa,
                            value = viewModel.busquedaInput.value,
                            onValueChanged = { viewModel.busquedaInput.value = it },
                            modifier = Modifier.fillMaxWidth(0.925f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )
                        FloatingActionButton(
                            onClick = { navController.navigate(Routes.NotasEditor.route) },
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar")
                        }
                    }
                } else {
                    // Barra de búsqueda en modo Portrait
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        BarraBusqueda(
                            label = R.string.busqueda,
                            leadingIcon = R.drawable.lupa,
                            value = viewModel.busquedaInput.value,
                            onValueChanged = { viewModel.busquedaInput.value = it },
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .fillMaxWidth(0.925f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            // Mostrar el FAB solo en modo Portrait
            if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                FloatingActionButton(onClick = { navController.navigate(Routes.NotasEditor.route) }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        HomeBody(
            notaList = homeUiState.itemList,
            onNotaClick = navigateToItemUpdate,
            busquedaInput = viewModel.busquedaInput.value,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    notaList: List<Nota>,
    onNotaClick: (Int) -> Unit,
    busquedaInput: String,
    modifier: Modifier = Modifier
) {
    // Filtrar las notas por la entrada de búsqueda y ordenar por fecha descendente
    val notasFiltradas = notaList.filter { it.name.contains(busquedaInput, ignoreCase = true) || busquedaInput.isEmpty() }
    val notasOrdenadas = notasFiltradas.sortedByDescending { it.fecha }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (notasFiltradas.isEmpty()) {
            // Mensaje si no hay notas disponibles
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            // Mostrar la lista de notas
            NotasList(
                notaList = notasOrdenadas,
                onNotaClick = { onNotaClick(it.id) },
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun NotasList(
    notaList: List<Nota>,
    onNotaClick: (Nota) -> Unit,
    modifier: Modifier = Modifier
) {
    // Mostrar las notas utilizando LazyColumn para eficiencia
    LazyColumn(modifier = modifier) {
        items(items = notaList, key = { it.id }) { nota ->
            InventoryNota(
                nota = nota,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onNotaClick(nota) }
            )
        }
    }
}

@Composable
private fun InventoryNota(
    nota: Nota,
    modifier: Modifier = Modifier
) {
    // Cada nota se muestra en un Card con detalles
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nota.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = nota.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = nota.fecha.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
