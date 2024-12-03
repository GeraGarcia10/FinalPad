package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.TareaMultimedia
import kotlinx.coroutines.flow.Flow

class OfflineTareaMultimediaRepository(
    private val tareaMultimediaDAO: TareaMultimediaDAO
) : TareaMultimediaRepository {

    // Recupera todas las tareas multimedia como un flujo
    override fun getAllItemsStream(): Flow<List<TareaMultimedia>> {
        // Solicita todas las tareas multimedia al DAO
        return tareaMultimediaDAO.getAllItems()
    }

    // Recupera una tarea multimedia específica por su id
    override fun getItemStream(id: Int): Flow<TareaMultimedia?> {
        // Solicita una tarea multimedia específica por id al DAO
        return tareaMultimediaDAO.getItem(id)
    }

    // Recupera todas las tareas multimedia asociadas a un id de tarea
    override fun getItemsStreamById(tareaId: Int): Flow<List<TareaMultimedia>> {
        // Solicita las tareas multimedia asociadas al id de la tarea
        return tareaMultimediaDAO.getAllById(tareaId)
    }

    // Inserta una tarea multimedia en la base de datos
    override suspend fun insertItem(tareaMultimedia: TareaMultimedia) {
        // Inserta una tarea multimedia a través del DAO
        tareaMultimediaDAO.insert(tareaMultimedia)
    }

    // Elimina una tarea multimedia de la base de datos
    override suspend fun deleteItem(tareaMultimedia: TareaMultimedia) {
        // Elimina la tarea multimedia a través del DAO
        tareaMultimediaDAO.delete(tareaMultimedia)
    }

    // Actualiza una tarea multimedia existente
    override suspend fun updateItem(tareaMultimedia: TareaMultimedia) {
        // Actualiza la tarea multimedia a través del DAO
        tareaMultimediaDAO.update(tareaMultimedia)
    }
}
