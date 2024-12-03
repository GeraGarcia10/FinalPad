package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.NotaMultimedia
import kotlinx.coroutines.flow.Flow

interface NotaMultimediaRepository {
    /**
     * Retrieve all items from the data source.
     */
    fun getAllItems(): Flow<List<NotaMultimedia>>

    /**
     * Retrieve a single item from the data source by [id].
     */
    fun getItemById(id: Int): Flow<NotaMultimedia?>

    /**
     * Retrieve all items from the data source by [notaId].
     */
    fun getItemsByNotaId(notaId: Int): Flow<List<NotaMultimedia>>

    /**
     * Insert an item into the data source.
     */
    suspend fun insert(notaMultimedia: NotaMultimedia)

    /**
     * Delete an item from the data source.
     */
    suspend fun delete(notaMultimedia: NotaMultimedia)

    /**
     * Update an item in the data source.
     */
    suspend fun update(notaMultimedia: NotaMultimedia)
}
