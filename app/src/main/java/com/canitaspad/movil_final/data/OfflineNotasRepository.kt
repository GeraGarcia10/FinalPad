package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.Nota
import kotlinx.coroutines.flow.Flow

class OfflineNotasRepository(
    private val notaDAO: NotaDAO
) : NotasRepository {

    // Obtener un flujo de todas las notas
    override fun getAllItemsStream(): Flow<List<Nota>> {
        // Llamada al DAO para obtener todas las notas
        return notaDAO.getAllItems()
    }

    // Obtener un flujo de una nota específica según su id
    override fun getItemStream(id: Int): Flow<Nota?> {
        // Llamada al DAO para obtener una nota específica
        return notaDAO.getItem(id)
    }

    // Insertar una nueva nota
    override suspend fun insertItem(nota: Nota) {
        // Llamada al DAO para insertar una nueva nota
        notaDAO.insert(nota)
    }

    // Eliminar una nota existente
    override suspend fun deleteItem(nota: Nota) {
        // Llamada al DAO para eliminar una nota
        notaDAO.delete(nota)
    }

    // Actualizar una nota existente
    override suspend fun updateItem(nota: Nota) {
        // Llamada al DAO para actualizar una nota
        notaDAO.update(nota)
    }

    // Insertar una nueva nota y devolver su id
    override suspend fun insertItemAndGetId(nota: Nota): Long {
        // Llamada al DAO para insertar la nota y obtener el id
        return notaDAO.insertAndGetId(nota)
    }
}
