package com.canitaspad.movil_final.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")  // Tabla de la base de datos denominada "tareas"
data class Tarea(
    // Identificador único de la tarea, autogenerado
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Nombre de la tarea
    @ColumnInfo(name = "name") var name: String,

    // Descripción de la tarea
    @ColumnInfo(name = "description") var description: String,

    // Fecha en que se creó la tarea
    @ColumnInfo(name = "fecha") val fecha: String,

    // Fecha en la que se debe completar la tarea
    @ColumnInfo(name = "fechaACompletar") val fechaACompletar: String,

    // Indicador de si la tarea está completa o no
    @ColumnInfo(name = "isComplete") var isComplete: Boolean,

    // Contenido adicional relacionado con la tarea
    @ColumnInfo(name = "contenido") var contenido: String,

    // URIs de imágenes asociadas con la tarea
    @ColumnInfo(name = "imageUris") var imageUris: String,

    // URIs de videos asociados con la tarea
    @ColumnInfo(name = "videoUris") var videoUris: String,

    // URIs de audios asociados con la tarea
    @ColumnInfo(name = "audioUris") var audioUris: String
)
