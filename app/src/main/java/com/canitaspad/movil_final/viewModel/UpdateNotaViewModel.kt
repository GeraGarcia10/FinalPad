package com.canitaspad.movil_final.viewModel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.canitaspad.movil_final.data.NotaMultimediaRepository
import com.canitaspad.movil_final.data.NotasRepository
import com.canitaspad.movil_final.model.NotaMultimedia
import com.canitaspad.movil_final.ui.NotaEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateNotaViewModel(
    savedStateHandle: SavedStateHandle,
    private val notasRepository: NotasRepository,
    val notasMultimediaRepository: NotaMultimediaRepository
) : ViewModel() {

    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())
    var audioUris by mutableStateOf(listOf<Uri>())

    private val _notaMultimediaUiState = mutableStateOf(NotaMultimediaUiState())
    val notaMultimediaUiState: NotaMultimediaUiState
        get() = _notaMultimediaUiState.value

    fun setNotaMultimediaUiState(newUiState: NotaMultimediaUiState) {
        _notaMultimediaUiState.value = newUiState
    }

    // Función genérica para eliminar URIs de los diferentes tipos
    fun removeUri(uri: Uri, type: UriType) {
        when (type) {
            UriType.IMAGE -> imageUris = imageUris.filter { it != uri }
            UriType.VIDEO -> videoUris = videoUris.filter { it != uri }
            UriType.AUDIO -> audioUris = audioUris.filter { it != uri }
        }
    }

    enum class UriType {
        IMAGE, VIDEO, AUDIO
    }

    var notaUiState by mutableStateOf(NotaUiState())
        private set

    private val notaId: Int = checkNotNull(savedStateHandle[NotaEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            // Cargar la nota desde el repositorio
            notaUiState = notasRepository.getItemStream(notaId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    val notaMultimedia: LiveData<List<NotaMultimedia>> = notasMultimediaRepository.getItemsStreamById(notaUiState.notaDetails.id).asLiveData()

    // Función para actualizar la nota en el repositorio
    suspend fun updateItem() {
        if (validateInput(notaUiState.notaDetails)) {
            notasRepository.updateItem(notaUiState.notaDetails.toItem())
        }
    }

    // Función para actualizar el estado de la UI con los nuevos detalles
    @SuppressLint("NewApi")
    fun updateUiState(itemDetails: NotaDetails) {
        // Formato de la fecha actual
        itemDetails.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        // Actualización de los detalles de la nota
        val updatedNotaDetails = itemDetails.copy(
            fecha = itemDetails.fecha,
            imageUris = imageUris.joinToString(","),
            videoUris = videoUris.joinToString(",")
        )
        // Actualizar el estado de la UI
        notaUiState = NotaUiState(notaDetails = updatedNotaDetails, isEntryValid = validateInput(itemDetails))
    }

    // Función genérica para validar las entradas de la nota
    private fun validateInput(uiState: NotaDetails = notaUiState.notaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && fecha.isNotBlank() && contenido.isNotBlank()
        }
    }
}




