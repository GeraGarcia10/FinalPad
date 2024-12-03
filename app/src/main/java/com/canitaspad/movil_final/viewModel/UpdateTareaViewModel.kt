package com.canitaspad.movil_final.viewModel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.TareaMultimediaRepository
import com.canitaspad.movil_final.data.TareasRepository
import com.canitaspad.movil_final.ui.TareaEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateTareaViewModel(
    savedStateHandle: SavedStateHandle,
    private val tareasRepository: TareasRepository,
    val tareasMultimediaRepository: TareaMultimediaRepository
) : ViewModel() {

    // Estado para el mensaje
    var mensaje = mutableStateOf("")

    // Función para actualizar el mensaje
    fun updateMensaje(nuevoMensaje: String) {
        mensaje.value = nuevoMensaje
    }

    // Estado de la UI de la tarea
    var tareaUiState by mutableStateOf(TareaUiState())
        private set

    // Listas de URIs para imágenes, videos y audios
    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())
    var audioUris by mutableStateOf(listOf<Uri>())

    // Estado de los multimedia de la tarea (para mostrar sus descripciones y URIs)
    private val _tareaMultimediaUiState = mutableStateOf(NotaMultimediaUiState())
    val tareaMultimediaUiState: NotaMultimediaUiState
        get() = _tareaMultimediaUiState.value

    // ID de la tarea que se obtiene desde el estado guardado
    private val tareaId: Int = checkNotNull(savedStateHandle[TareaEditDestination.itemIdArg])

    init {
        // Cargar la tarea cuando se inicialice el ViewModel
        viewModelScope.launch {
            tareaUiState = tareasRepository.getItemStream(tareaId)
                .filterNotNull()  // Asegurarse de que la tarea no sea nula
                .first()  // Obtener el primer valor (ya que es un stream)
                .toItemUiState(true)  // Convertir la tarea a un estado adecuado para la UI
        }
    }

    /**
     * Actualiza el elemento en la base de datos del [TareasRepository]
     */
    suspend fun updateItem() {
        if (validateInput(tareaUiState.tareaDetails)) {
            // Si los datos son válidos, actualiza la tarea
            tareasRepository.updateItem(tareaUiState.tareaDetails.toItem())
        }
    }

    // Función para eliminar una URI de los multimedia (imágenes, videos, etc.)
    fun removeUri(uri: Uri) {
        imageUris = imageUris.filter { it != uri }
        videoUris = videoUris.filter { it != uri }
        audioUris = audioUris.filter { it != uri }
    }

    /**
     * Actualiza el estado de la tarea con los detalles proporcionados.
     * Este método también valida los valores de entrada.
     */
    @SuppressLint("NewApi")
    fun updateUiState(itemDetails: TareaDetails, selectedDate: String) {
        itemDetails.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        itemDetails.fechaACompletar = selectedDate
        val updatedTareaDetails = itemDetails.copy(
            fecha = itemDetails.fecha,
            fechaACompletar = itemDetails.fechaACompletar,
            imageUris = imageUris.joinToString(","),
            videoUris = videoUris.joinToString(",")
        )
        tareaUiState = TareaUiState(tareaDetails = updatedTareaDetails, isEntryValid = validateInput(itemDetails))
    }

    // Función de validación para asegurar que los campos de la tarea no estén vacíos
    private fun validateInput(uiState: TareaDetails = tareaUiState.tareaDetails): Boolean {
        return with(uiState) {
            // Comprobamos que todos los campos relevantes no estén vacíos
            name.isNotBlank() && fecha.isNotBlank() && contenido.isNotBlank()
        }
    }
}

