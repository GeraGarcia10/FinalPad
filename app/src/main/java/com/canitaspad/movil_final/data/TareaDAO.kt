package com.canitaspad.movil_final.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.canitaspad.movil_final.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDAO {

    // Insertar una nueva tarea, ignorando conflictos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tarea: Tarea)

    // Insertar una nueva tarea y obtener su id generado
    @Insert
    suspend fun insertAndGetId(tarea: Tarea): Long

    // Actualizar una tarea existente
    @Update
    suspend fun update(tarea: Tarea)

    // Eliminar una tarea
    @Delete
    suspend fun delete(tarea: Tarea)

    // Recuperar una tarea por su id
    @Query("SELECT * from tareas WHERE id = :id")
    fun getItem(id: Int): Flow<Tarea>

    // Obtener todas las tareas ordenadas por nombre de manera ascendente
    @Query("SELECT * from tareas ORDER BY name ASC")
    fun getAllItems(): Flow<List<Tarea>>
}
