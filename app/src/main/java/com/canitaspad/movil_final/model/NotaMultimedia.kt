package com.canitaspad.movil_final.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notaMultimedia",
    foreignKeys = [
        ForeignKey(
            entity = Nota::class,  // Referencia a la entidad 'Nota'
            parentColumns = ["id"],  // Columna de la tabla 'Nota' que actúa como clave primaria
            childColumns = ["notaId"],  // Columna de la tabla 'notaMultimedia' que actúa como clave foránea
            onDelete = ForeignKey.CASCADE  // Si se elimina una 'Nota', se eliminan sus 'NotaMultimedia' asociadas
        )
    ]
)
data class NotaMultimedia(
    // Identificador único de la nota multimedia, autogenerado
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // URI del archivo multimedia
    @ColumnInfo(name = "uri") var uri: String,

    // Descripción del archivo multimedia
    @ColumnInfo(name = "descripcion") var descripcion: String,

    // ID de la nota a la que pertenece esta multimedia
    @ColumnInfo(name = "notaId") var notaId: Int,

    // Tipo de archivo multimedia (ej. "imagen", "video", "audio")
    @ColumnInfo(name = "tipo") var tipo: String
)
