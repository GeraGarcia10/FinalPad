package com.canitaspad.movil_final.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    // Identificador único de la nota, autogenerado
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Nombre de la nota
    @ColumnInfo(name = "name") var name: String,

    // Descripción de la nota
    @ColumnInfo(name = "description") var description: String,

    // Fecha en la que se creó o modificó la nota
    @ColumnInfo(name = "fecha") val fecha: String,

    // Contenido de la nota
    @ColumnInfo(name = "contenido") var contenido: String,

    // Rutas de las imágenes asociadas a la nota (almacenadas como una cadena)
    @ColumnInfo(name = "imageUris") var imageUris: String,

    // Rutas de los videos asociados a la nota (almacenadas como una cadena)
    @ColumnInfo(name = "videoUris") var videoUris: String,

    // Rutas de los audios asociados a la nota (almacenadas como una cadena)
    @ColumnInfo(name = "audioUris") var audioUris: String
)

