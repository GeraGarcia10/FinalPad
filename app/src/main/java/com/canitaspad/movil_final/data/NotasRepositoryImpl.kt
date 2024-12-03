package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.Nota
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotasRepositoryImpl : NotasRepository {

    // Devolver todas las notas como un flujo
    override fun getAllItemsStream(): Flow<List<Nota>> = flow {
        // Aquí iría la implementación para devolver todas las notas desde la base de datos
        emit(emptyList())  // Temporarily returning an empty list for the flow
    }

    // Devolver una nota específica por id como un flujo
    override fun getItemStream(id: Int): Flow<Nota?> = flow {
        // Aquí iría la implementación para buscar una nota por su id
        emit(null)  // Temporarily returning null for the flow
    }

    // Insertar una nueva nota en la base de datos
    override suspend fun insertItem(nota: Nota) {
        // Implementación para insertar la nota
    }

    // Insertar una nueva nota y obtener su id
    override suspend fun insertItemAndGetId(nota: Nota): Long {
        // Implementación para insertar y retornar el id de la nota
        return 0L // Temporarily returning a default id
    }

    // Eliminar una nota
    override suspend fun deleteItem(nota: Nota) {
        // Implementación para eliminar la nota
    }

    // Actualizar una nota existente
    override suspend fun updateItem(nota: Nota) {
        // Implementación para actualizar la nota
    }
}
