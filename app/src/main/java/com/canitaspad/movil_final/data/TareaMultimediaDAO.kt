package com.canitaspad.movil_final.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.canitaspad.movil_final.model.TareaMultimedia
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaMultimediaDAO {

    // Insertar un nuevo elemento de tarea multimedia, ignorando conflictos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tareaMultimedia: TareaMultimedia)

    // Actualizar una tarea multimedia existente
    @Update
    suspend fun update(tareaMultimedia: TareaMultimedia)

    // Eliminar una tarea multimedia
    @Delete
    suspend fun delete(tareaMultimedia: TareaMultimedia)

    // Obtener una tarea multimedia específica por su id
    @Query("SELECT * from tareaMultimedia WHERE id = :id")
    fun getItem(id: Int): Flow<TareaMultimedia>

    // Obtener todas las tareas multimedia asociadas a un id de tarea
    @Query("SELECT * from tareaMultimedia WHERE tareaId = :tareaId")
    fun getAllById(tareaId: Int): Flow<List<TareaMultimedia>>

    // Obtener todas las tareas multimedia
    @Query("SELECT * from tareaMultimedia")
    fun getAllItems(): Flow<List<TareaMultimedia>>
}
