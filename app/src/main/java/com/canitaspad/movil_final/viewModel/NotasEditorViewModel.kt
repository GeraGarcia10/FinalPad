package com.canitaspad.movil_final.viewModel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.canitaspad.movil_final.data.NotaMultimediaRepository
import com.canitaspad.movil_final.data.NotasRepository
import com.canitaspad.movil_final.model.Nota
import com.canitaspad.movil_final.model.NotaMultimedia
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class NotasEditorViewModel(
    private val notasRepository: NotasRepository,
    private val notasMultimediaRepository: NotaMultimediaRepository
) : ViewModel() {

    // Descripción del multimedia con su URI y una descripción personalizada
    data class MultimediaDescription(val uri: Uri, var descripcion: String)

    // Estado mutable para manejar las descripciones de los archivos multimedia
    var multimediaDescriptions by mutableStateOf(mutableListOf<MultimediaDescription>())
    // Estado mutable para gestionar el estado de la UI de la nota
    var notaUiState by mutableStateOf(NotaUiState())
        private set

    // Archivo de salida para el manejo de archivos multimedia
    var outputFile: File? = null

    // Función para actualizar el archivo de salida
    fun updateOutputFile(nuevoArchivo: File) {
        outputFile = nuevoArchivo
    }

    // URIs de imágenes, videos y audios que se adjuntan a la nota
    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())
    var audioUris by mutableStateOf(listOf<Uri>())

    /**
     * Actualiza el estado de la UI con los detalles de la nota, incluyendo las URIs de medios.
     * También actualiza la fecha actual y valida la entrada.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiState(notaDetails: NotaDetails) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        // Se actualiza la nota con la fecha actual y las URIs de medios convertidas en cadenas
        val updatedNotaDetails = notaDetails.copy(
            fecha = currentDateTime,
            imageUris = imageUris.joinToString(","),
            videoUris = videoUris.joinToString(","),
            audioUris = audioUris.joinToString(",")
        )
        // Se actualiza el estado de la UI con la nueva nota
        notaUiState = NotaUiState(notaDetails = updatedNotaDetails, isEntryValid = validateInput(updatedNotaDetails))
    }

    /**
     * Elimina una URI específica de los tipos de medios asociados a la nota.
     */
    fun removeUri(uri: Uri) {
        // Filtra y elimina la URI de las listas correspondientes
        imageUris = imageUris.filter { it != uri }
        videoUris = videoUris.filter { it != uri }
        audioUris = audioUris.filter { it != uri }
    }

    /**
     * Valida la entrada del usuario asegurando que los campos necesarios no estén vacíos.
     */
    private fun validateInput(uiState: NotaDetails = notaUiState.notaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && contenido.isNotBlank() && fecha.isNotBlank()
        }
    }

    /**
     * Guarda la nota junto con sus archivos multimedia asociados.
     * - Guarda los detalles de la nota en el repositorio.
     * - Guarda las descripciones de los archivos multimedia asociados.
     */
    suspend fun saveNota() {
        if (validateInput()) {
            // Guarda la nota y obtiene el ID.
            val notaId = notasRepository.insertItemAndGetId(notaUiState.notaDetails.toItem())

            // Guarda las imágenes asociadas con su descripción
            for ((index, uri) in (imageUris).withIndex()) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val notaMultimedia = NotaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    notaId = notaId.toInt(),
                    tipo = "imagen"
                )
                // Inserta la multimedia en el repositorio
                notasMultimediaRepository.insertItem(notaMultimedia)
            }

            // Guarda los videos asociados con su descripción
            for ((index, uri) in (videoUris).withIndex()) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val notaMultimedia = NotaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    notaId = notaId.toInt(),
                    tipo = "video"
                )
                // Inserta la multimedia en el repositorio
                notasMultimediaRepository.insertItem(notaMultimedia)
            }

            // Guarda los audios asociados con su descripción
            for (uri in audioUris) {
                val multimediaDescription = multimediaDescriptions
                    .firstOrNull { it.uri == uri }
                    ?: MultimediaDescription(uri, "Sin descripción")

                val notaMultimedia = NotaMultimedia(
                    uri = uri.toString(),
                    descripcion = multimediaDescription.descripcion,
                    notaId = notaId.toInt(),
                    tipo = "audio"
                )
                // Inserta la multimedia en el repositorio
                notasMultimediaRepository.insertItem(notaMultimedia)
            }
        }
    }
}

// Estado de la UI para los detalles de la nota
data class NotaUiState(
    val notaDetails: NotaDetails = NotaDetails(),
    val isEntryValid: Boolean = false
)

// Detalles de una nota
data class NotaDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    var fecha: String = "",
    val contenido: String = "",
    val imageUris: String = "",
    val videoUris: String = "",
    val audioUris: String = ""
)

// Mapea los detalles de una nota a un objeto de tipo Nota
fun NotaDetails.toItem(): Nota = Nota(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    contenido = contenido,
    imageUris = imageUris,
    videoUris = videoUris,
    audioUris = audioUris
)

// Mapea una Nota a su representación UI
fun Nota.toItemUiState(isEntryValid: Boolean = false): NotaUiState = NotaUiState(
    notaDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

// Mapea una Nota a sus detalles UI
fun Nota.toItemDetails(): NotaDetails = NotaDetails(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    contenido = contenido,
    imageUris = imageUris,
    videoUris = videoUris,
    audioUris = audioUris
)

// Detalles de un multimedia asociado a una nota
data class NotaMultimediaUiState(
    val notaMultimediaDetails: NotaMultimediaDetails = NotaMultimediaDetails(),
    val multimediaDescriptions: MutableMap<String, String> = mutableMapOf()
)

// Detalles de un multimedia
data class NotaMultimediaDetails(
    val id: Int = 0,
    val uri: String = "",
    var descripcion: String = "",
    var notaId: Int = 0,
    var tipo: String = ""
)

// Mapea los detalles de un multimedia a un objeto de tipo NotaMultimedia
fun NotaMultimediaDetails.toItem(): NotaMultimedia = NotaMultimedia(
    id = id,
    uri = uri,
    descripcion = descripcion,
    notaId = notaId,
    tipo = tipo
)

// Mapea un NotaMultimedia a su representación UI
fun NotaMultimedia.toItemUiState(): NotaMultimediaUiState = NotaMultimediaUiState(
    notaMultimediaDetails = this.toItemDetails()
)

// Mapea un NotaMultimedia a sus detalles UI
fun NotaMultimedia.toItemDetails(): NotaMultimediaDetails = NotaMultimediaDetails(
    id = id,
    uri = uri,
    descripcion = descripcion,
    notaId = notaId,
    tipo = tipo
)

