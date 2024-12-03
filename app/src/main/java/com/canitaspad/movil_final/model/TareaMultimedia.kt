package com.canitaspad.movil_final.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tareaMultimedia",  // Tabla de la base de datos denominada "tareaMultimedia"
    foreignKeys = [ForeignKey(
        entity = Tarea::class,  // Relacionado con la entidad "Tarea"
        parentColumns = ["id"],  // Clave primaria de la entidad "Tarea"
        childColumns = ["tareaId"],  // Clave foránea en "TareaMultimedia"
        onDelete = ForeignKey.CASCADE  // Eliminar las multimedia cuando se elimina la tarea
    )]
)
data class TareaMultimedia(
    // Identificador único de la tarea multimedia, autogenerado
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // URI de la multimedia asociada con la tarea
    @ColumnInfo(name = "uri") var uri: String,

    // Descripción de la multimedia
    @ColumnInfo(name = "descripcion") var descripcion: String,

    // ID de la tarea asociada con la multimedia
    @ColumnInfo(name = "tareaId") var tareaId: Int,

    // Tipo de la multimedia (ejemplo: imagen, video, audio)
    @ColumnInfo(name = "tipo") var tipo: String
)
