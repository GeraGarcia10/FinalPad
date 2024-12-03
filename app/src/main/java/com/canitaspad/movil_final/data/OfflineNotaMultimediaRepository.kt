package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.NotaMultimedia
import kotlinx.coroutines.flow.Flow

class OfflineNotaMultimediaRepository(
    private val notaMultimediaDAO: NotaMultimediaDAO
) : NotaMultimediaRepository {

    // Método para obtener todos los elementos como un flujo
    override fun getAllItemsStream(): Flow<List<NotaMultimedia>> {
        // Llamada al DAO para obtener todos los elementos de notaMultimedia
        return notaMultimediaDAO.getAllItems()
    }

    // Método para obtener un solo elemento específico por id
    override fun getItemStream(id: Int): Flow<NotaMultimedia?> {
        // Llamada al DAO para obtener una notaMultimedia específica por id
        return notaMultimediaDAO.getItem(id)
    }

    // Método para obtener todos los elementos relacionados con un id de nota
    override fun getItemsStreamById(notaId: Int): Flow<List<NotaMultimedia>> {
        // Llamada al DAO para obtener las multimedia asociadas con un id de nota
        return notaMultimediaDAO.getAllById(notaId)
    }

    // Método para insertar una nueva nota multimedia
    override suspend fun insertItem(notaMultimedia: NotaMultimedia) {
        // Inserción de la nota multimedia a través del DAO
        notaMultimediaDAO.insert(notaMultimedia)
    }

    // Método para eliminar una nota multimedia existente
    override suspend fun deleteItem(notaMultimedia: NotaMultimedia) {
        // Eliminación de la nota multimedia a través del DAO
        notaMultimediaDAO.delete(notaMultimedia)
    }

    // Método para actualizar una nota multimedia existente
    override suspend fun updateItem(notaMultimedia: NotaMultimedia) {
        // Actualización de la nota multimedia a través del DAO
        notaMultimediaDAO.update(notaMultimedia)
    }
}
