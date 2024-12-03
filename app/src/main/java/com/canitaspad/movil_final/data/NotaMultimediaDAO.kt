package com.canitaspad.movil_final.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.canitaspad.movil_final.model.NotaMultimedia
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaMultimediaDAO {
    // Insertar sin duplicados
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notaMultimedia: NotaMultimedia)

    // Actualizar una entrada
    @Update
    suspend fun update(notaMultimedia: NotaMultimedia)

    // Eliminar una entrada
    @Delete
    suspend fun delete(notaMultimedia: NotaMultimedia)

    // Obtener una entrada por ID, devolviendo Flow<NotaMultimedia?> por si es nulo
    @Query("SELECT * from notaMultimedia WHERE id = :id")
    fun getItem(id: Int): Flow<NotaMultimedia?>

    // Obtener todas las entradas de una nota específica
    @Query("SELECT * from notaMultimedia WHERE notaId = :notaId")
    fun getAllById(notaId: Int): Flow<List<NotaMultimedia>>

    // Obtener todas las entradas
    @Query("SELECT * from notaMultimedia")
    fun getAllItems(): Flow<List<NotaMultimedia>>
}
