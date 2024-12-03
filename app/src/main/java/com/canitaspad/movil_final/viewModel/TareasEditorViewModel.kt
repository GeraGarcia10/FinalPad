package com.canitaspad.movil_final.viewModel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.canitaspad.movil_final.data.TareaMultimediaRepository
import com.canitaspad.movil_final.data.TareasRepository
import com.canitaspad.movil_final.model.Tarea
import com.canitaspad.movil_final.model.TareaMultimedia
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TareasEditorViewModel(
    private val tareasRepository: TareasRepository,
    private val tareasMultimediaRepository: TareaMultimediaRepository
) : ViewModel() {

    // Representa una descripción de los archivos multimedia (imagen, video, audio)
    data class MultimediaDescription(val uri: Uri, var descripcion: String)

    // Lista mutable de descripciones de archivos multimedia
    var multimediaDescriptions by mutableStateOf(mutableListOf<MultimediaDescription>())

    // Archivo de salida que se puede actualizar
    var outputFile: File? = null

    fun updateOutputFile(nuevoArchivo: File) {
        outputFile = nuevoArchivo
    }

    // Mensaje mutable, por ejemplo, para mostrar mensajes o alertas
    var mensaje = mutableStateOf("")

    fun updateMensaje(nuevoMensaje: String) {
        mensaje.value = nuevoMensaje
    }

    // Estado para manejar la UI relacionada con los detalles de la tarea
    private val _tareaMultimediaUiState = mutableStateOf(TareaMultimediaUiState())
    var tareaUiState by mutableStateOf(TareaUiState()) // Estado de la UI de la tarea
        private set

    // Listas para almacenar las URIs de los archivos multimedia (imágenes, videos, audios)
    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())
    var audioUris by mutableStateOf(listOf<Uri>())

    // Elimina un URI de las listas de archivos multimedia
    fun removeUri(uri: Uri) {
        imageUris = imageUris.filter { it != uri }
        videoUris = videoUris.filter { it != uri }
        audioUris = audioUris.filter { it != uri }
    }

    // Actualiza el estado de la UI de la tarea, incluyendo la fecha actual y las URIs de los archivos multimedia
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiState(tareaDetails: TareaDetails, selectedDate: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedTareaDetails = tareaDetails.copy(
            fecha = currentDateTime,
            fechaACompletar = selectedDate,
            imageUris = imageUris.joinToString(","),
            videoUris = videoUris.joinToString(","),
            audioUris = audioUris.joinToString(",")
        )
        tareaUiState = TareaUiState(tareaDetails = updatedTareaDetails, isEntryValid = validateInput(updatedTareaDetails))
    }

    // Valida que los datos de la tarea sean correctos antes de guardarlos
    private fun validateInput(uiState: TareaDetails = tareaUiState.tareaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && contenido.isNotBlank() && fecha.isNotBlank() // Verifica que no estén vacíos los campos importantes
        }
    }

    // Guarda la tarea en el repositorio y también guarda los archivos multimedia asociados
    suspend fun saveTarea() {
        if (validateInput()) {
            // Inserta la tarea en el repositorio y obtiene el ID generado
            val tareaId = tareasRepository.insertItemAndGetId(tareaUiState.tareaDetails.toItem())

            // Guarda los archivos multimedia asociados a la tarea (imágenes)
            for ((index, uri) in imageUris.withIndex()) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val tareaMultimedia = TareaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    tareaId = tareaId.toInt(),
                    tipo = "imagen"
                )
                tareasMultimediaRepository.insertItem(tareaMultimedia)
            }

            // Guarda los archivos multimedia asociados a la tarea (videos)
            for ((index, uri) in videoUris.withIndex()) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val tareaMultimedia = TareaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    tareaId = tareaId.toInt(),
                    tipo = "video"
                )
                tareasMultimediaRepository.insertItem(tareaMultimedia)
            }

            // Guarda los archivos multimedia asociados a la tarea (audios)
            for (uri in audioUris) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val tareaMultimedia = TareaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    tareaId = tareaId.toInt(),
                    tipo = "audio"
                )
                tareasMultimediaRepository.insertItem(tareaMultimedia)
            }
        }
    }
}

data class TareaUiState(
    val tareaDetails: TareaDetails = TareaDetails(),
    val isEntryValid: Boolean = false
)

data class TareaDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    var fecha: String = "",
    var fechaACompletar: String = "",
    val contenido: String = "",
    val isComplite: Boolean = false,
    val imageUris: String = "",
    val videoUris: String = "",
    val audioUris: String = ""
)

fun TareaDetails.toItem(): Tarea = Tarea(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    fechaACompletar = fechaACompletar,
    contenido = contenido,
    isComplete = isComplite,
    imageUris = imageUris,
    videoUris = videoUris,
    audioUris = audioUris
)

fun Tarea.toItemUiState(isEntryValid: Boolean = false): TareaUiState = TareaUiState(
    tareaDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Tarea.toItemDetails(): TareaDetails = TareaDetails(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    fechaACompletar = fechaACompletar,
    contenido = contenido,
    isComplite = isComplete,
    imageUris = imageUris,
    videoUris = videoUris,
    audioUris = audioUris
)

//MULTIMEDIA
data class TareaMultimediaUiState(
    val tareaMultimediaDetails: TareaMultimediaDetails = TareaMultimediaDetails(),
    val multimediaDescriptions: MutableMap<String, String> = mutableMapOf()
)

data class TareaMultimediaDetails(
    val id: Int = 0,
    val uri: String = "",
    var descripcion: String = "",
    var tareaId: Int = 0,
    var tipo: String = ""
)

fun TareaMultimediaDetails.toItem(): TareaMultimedia = TareaMultimedia(
    id = id,
    uri = uri,
    descripcion = descripcion,
    tareaId = tareaId,
    tipo = tipo
)

fun TareaMultimedia.toItemUiState(): TareaMultimediaUiState = TareaMultimediaUiState(
    tareaMultimediaDetails = this.toItemDetails()
)

fun TareaMultimedia.toItemDetails(): TareaMultimediaDetails = TareaMultimediaDetails(
    id = id,
    uri = uri,
    descripcion = descripcion,
    tareaId = tareaId,
    tipo = tipo
)

