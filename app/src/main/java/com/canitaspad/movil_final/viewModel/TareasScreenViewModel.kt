package com.canitaspad.movil_final.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.TareasRepository
import com.canitaspad.movil_final.model.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TareasScreenViewModel(tareasRepository: TareasRepository): ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        tareasRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    data class HomeUiState(val itemList: List<Tarea> = listOf())

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        initializeUIState()
    }

    private fun initializeUIState() {
        AppUiState(
            cantidadNotas = 0,
            cantidadTareas = 0,
            cantidadTareasComp = 0
        )
    }

    val busquedaInput = mutableStateOf("")
}