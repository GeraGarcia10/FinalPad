package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.Nota
import kotlinx.coroutines.flow.Flow

interface NotasRepository {
    /**
     * Obtener todos los elementos de la fuente de datos.
     */
    fun getAllItemsStream(): Flow<List<Nota>>

    /**
     * Obtener un elemento de la fuente de datos que coincida con el [id].
     */
    fun getItemStream(id: Int): Flow<Nota?>

    /**
     * Insertar un elemento en la fuente de datos.
     */
    suspend fun insertItem(nota: Nota)

    /**
     * Eliminar un elemento de la fuente de datos.
     */
    suspend fun deleteItem(nota: Nota)

    /**
     * Actualizar un elemento en la fuente de datos.
     */
    suspend fun updateItem(nota: Nota)

    /**
     * Insertar un elemento y obtener su [id].
     */
    suspend fun insertItemAndGetId(nota: Nota): Long
}
