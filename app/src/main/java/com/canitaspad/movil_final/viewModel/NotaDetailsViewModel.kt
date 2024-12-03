package com.canitaspad.movil_final.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.NotasRepository
import com.canitaspad.movil_final.ui.NotasDetallesDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val notasRepository: NotasRepository,
) : ViewModel() {

    // Se obtiene el ID de la nota desde los argumentos de la pantalla de detalles.
    private val notaId: Int = checkNotNull(savedStateHandle[NotasDetallesDestination.notaIdArg])

    /**
     * Estado de la UI que contiene los detalles de la nota.
     * Los datos son obtenidos desde el [NotasRepository] y mapeados al estado de la UI.
     *
     * Utilizamos un flujo de estado [StateFlow] para observar cambios en el estado de la nota.
     * - [filterNotNull()]: Filtra los valores nulos (solo obtiene valores válidos).
     * - [map()]: Transforma los datos obtenidos en el estado adecuado para la UI.
     * - [stateIn()]: Convierte el flujo en un estado observable que se mantiene en la memoria.
     */
    val uiState: StateFlow<NotaDetailsUiState> =
        notasRepository.getItemStream(notaId)
            .filterNotNull() // Filtra valores nulos
            .map {
                // Mapea el modelo de la nota a un formato adecuado para la UI
                NotaDetailsUiState(notaDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope, // Se mantiene en el scope de este ViewModel
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), // Inicia el flujo cuando la UI se suscribe y lo mantiene activo durante un tiempo
                initialValue = NotaDetailsUiState() // Valor inicial vacío
            )

    /**
     * Elimina la nota actual del repositorio de notas.
     * Llama a [deleteItem()] de [NotasRepository] para eliminar la nota seleccionada.
     */
    suspend fun deleteItem() {
        // Se usa el estado actual de la UI para obtener los detalles de la nota y eliminarla
        notasRepository.deleteItem(uiState.value.notaDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L // Tiempo de suscripción antes de que se libere la memoria
    }
}

/**
 * UI state para la pantalla de detalles de la nota.
 * Se utiliza para almacenar los detalles de la nota a mostrar en la interfaz.
 */
data class NotaDetailsUiState(
    val notaDetails: NotaDetails = NotaDetails() // Valor por defecto de la nota
)
