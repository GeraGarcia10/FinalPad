package com.canitaspad.movil_final.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.TareasRepository
import com.canitaspad.movil_final.ui.TareasDetallesDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareaDetailsViewModel(savedStateHandle: SavedStateHandle,
                            private val tareasRepository: TareasRepository,
) : ViewModel() {

    // Obtiene el ID de la tarea desde el estado guardado para recuperar los detalles correspondientes.
    private val tareaId: Int = checkNotNull(savedStateHandle[TareasDetallesDestination.tareaIdArg])

    /**
     * Estado UI que contiene los detalles de la tarea. Los datos se obtienen de [TareasRepository]
     * y se mapean al estado de la UI.
     */
    val uiState: StateFlow<TareaDetailsUiState> =
        tareasRepository.getItemStream(tareaId) // Obtiene un flujo de datos de la tarea
            .filterNotNull() // Filtra valores nulos para asegurar que siempre haya un valor válido
            .map {
                TareaDetailsUiState(tareaDetails = it.toItemDetails()) // Mapea los detalles de la tarea a UI state
            }.stateIn(
                scope = viewModelScope, // El flujo se ejecuta en el contexto del ViewModel
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), // Mantiene la suscripción activa durante un tiempo
                initialValue = TareaDetailsUiState() // Estado inicial vacío para la tarea
            )

    /**
     * Elimina la tarea desde la fuente de datos del repositorio.
     */
    suspend fun deleteItem() {
        tareasRepository.deleteItem(uiState.value.tareaDetails.toItem()) // Elimina la tarea mediante su ID
    }

    /**
     * Marca la tarea como completada o no completada según su estado actual.
     */
    fun markComplete() {
        viewModelScope.launch {
            val currentItem = uiState.value.tareaDetails.toItem() // Obtiene el ítem de tarea actual
            tareasRepository.updateItem(currentItem.copy(isComplete = !currentItem.isComplete)) // Cambia el estado de completada
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L // Tiempo de suscripción del flujo
    }
}

/**
 * Estado de la UI para la pantalla de detalles de la tarea.
 */
data class TareaDetailsUiState(
    val complete: Boolean = true, // Marca si la tarea está completa
    val tareaDetails: TareaDetails = TareaDetails() // Detalles de la tarea
)
