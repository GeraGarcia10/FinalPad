package com.canitaspad.movil_final.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.NotasRepository
import com.canitaspad.movil_final.model.Nota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotasScreenViewModel(notasRepository: NotasRepository): ViewModel() {

    // Estado que gestiona los elementos obtenidos del repositorio para la pantalla principal.
    // Se observa el flujo de todas las notas disponibles desde el repositorio.
    val homeUiState: StateFlow<HomeUiState> =
        notasRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope, // El flujo se ejecuta en el scope del ViewModel
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), // Mantiene la suscripción activa durante un tiempo
                initialValue = HomeUiState() // Estado inicial vacío
            )

    // Constante que define el tiempo de suscripción antes de que se libere la memoria.
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // Estado que representa la lista de notas en la pantalla de inicio.
    data class HomeUiState(val itemList: List<Nota> = listOf())

    // Estado privado que gestiona el estado general de la aplicación.
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    // Inicializa el estado de la UI de la aplicación.
    init {
        initializeUIState()
    }

    /**
     * Inicializa el estado de la UI con los valores predeterminados para las cantidades de notas y tareas.
     */
    private fun initializeUIState() {
        // Asigna el valor inicial al estado de la aplicación
        _uiState.value = AppUiState(
            cantidadNotas = 0,
            cantidadTareas = 0,
            cantidadTareasComp = 0
        )
    }

    // Variable que mantiene el texto de búsqueda introducido por el usuario.
    val busquedaInput = mutableStateOf("")
}

/**
 * Estado de la UI para la aplicación.
 * Representa la cantidad de notas y tareas en la aplicación.
 */
data class AppUiState(
    val cantidadNotas: Int = 0,
    val cantidadTareas: Int = 0,
    val cantidadTareasComp: Int = 0
)
