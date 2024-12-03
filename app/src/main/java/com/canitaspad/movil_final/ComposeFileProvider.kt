package com.canitaspad.movil_final

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider(R.xml.file_paths) {

    companion object {

        // Método común para obtener una URI de archivo temporal, recibiendo la extensión
        private fun getTempFileUri(context: Context, extension: String): Uri {
            // 1. Crear un directorio específico para imágenes o videos dentro del cache
            val directory = File(context.cacheDir, if (extension == ".jpg") "images" else "videos")
            directory.mkdirs() // Asegura que el directorio exista

            // 2. Crear un archivo temporal con el prefijo y la extensión proporcionada
            val file = File.createTempFile(
                "selected_file_",
                extension,
                directory
            )

            // 3. Obtener la autoridad para el FileProvider
            val authority = "com.canitaspad.movil_final.fileprovider"

            // 4. Retornar la URI correspondiente al archivo
            return getUriForFile(context, authority, file)
        }

        // Método específico para obtener la URI de una imagen
        fun getImageUri(context: Context): Uri {
            return getTempFileUri(context, ".jpg")
        }

        // Método específico para obtener la URI de un video
        fun getVideoUri(context: Context): Uri {
            return getTempFileUri(context, ".mp4")
        }
    }
}
