package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.TareaMultimedia
import kotlinx.coroutines.flow.Flow

interface TareaMultimediaRepository {

    /**
     * Recupera todos los elementos de la fuente de datos proporcionada.
     */
    fun getAllItemsStream(): Flow<List<TareaMultimedia>>

    /**
     * Recupera un elemento de la fuente de datos que coincida con el [id].
     */
    fun getItemStream(id: Int): Flow<TareaMultimedia?>

    /**
     * Recupera todos los elementos de la fuente de datos que coincidan con el [tareaId].
     */
    fun getItemsStreamById(tareaId: Int): Flow<List<TareaMultimedia>>

    /**
     * Inserta un elemento en la fuente de datos.
     */
    suspend fun insertItem(tareaMultimedia: TareaMultimedia)

    /**
     * Elimina un elemento de la fuente de datos.
     */
    suspend fun deleteItem(tareaMultimedia: TareaMultimedia)

    /**
     * Actualiza un elemento en la fuente de datos.
     */
    suspend fun updateItem(tareaMultimedia: TareaMultimedia)

    // suspend fun getAllById(idTarea: Int)
}
