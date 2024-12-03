package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.Tarea
import kotlinx.coroutines.flow.Flow

interface TareasRepository {

    /**
     * Recupera todos los elementos desde la fuente de datos proporcionada.
     */
    fun getAllItemsStream(): Flow<List<Tarea>>

    /**
     * Recupera un elemento de la fuente de datos que coincida con el [id].
     */
    fun getItemStream(id: Int): Flow<Tarea?>

    /**
     * Inserta un elemento en la fuente de datos.
     */
    suspend fun insertItem(tarea: Tarea)

    /**
     * Elimina un elemento de la fuente de datos.
     */
    suspend fun deleteItem(tarea: Tarea)

    /**
     * Actualiza un elemento en la fuente de datos.
     */
    suspend fun updateItem(tarea: Tarea)

    /**
     * Inserta un elemento en la fuente de datos y devuelve el id generado.
     */
    suspend fun insertItemAndGetId(tarea: Tarea): Long
}
