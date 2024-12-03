package com.canitaspad.movil_final.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.canitaspad.movil_final.model.Nota
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDAO {
    // Insertar sin duplicados
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    // Insertar y obtener el ID
    @Insert
    suspend fun insertAndGetId(nota: Nota): Long

    // Actualizar una nota
    @Update
    suspend fun update(nota: Nota)

    // Eliminar una nota
    @Delete
    suspend fun delete(nota: Nota)

    // Obtener una nota por ID (devolviendo Flow<Nota?> si puede ser null)
    @Query("SELECT * from notas WHERE id = :id")
    fun getItem(id: Int): Flow<Nota?>

    // Obtener todas las notas ordenadas por nombre (añadido DISTINCT si es necesario)
    @Query("SELECT DISTINCT * from notas ORDER BY name ASC")
    fun getAllItems(): Flow<List<Nota>>
}
