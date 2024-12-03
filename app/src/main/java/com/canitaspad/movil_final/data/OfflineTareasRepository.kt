package com.canitaspad.movil_final.data

import com.canitaspad.movil_final.model.Tarea
import kotlinx.coroutines.flow.Flow

class OfflineTareasRepository(
    private val tareaDao: TareaDAO
) : TareasRepository {

    // Recuperar todas las tareas como un flujo
    override fun getAllItemsStream(): Flow<List<Tarea>> {
        // Solicita todas las tareas desde el DAO
        return tareaDao.getAllItems()
    }

    // Recuperar una tarea específica por su id
    override fun getItemStream(id: Int): Flow<Tarea?> {
        // Solicita una tarea específica desde el DAO
        return tareaDao.getItem(id)
    }

    // Insertar una nueva tarea
    override suspend fun insertItem(tarea: Tarea) {
        // Inserta una tarea en la base de datos a través del DAO
        tareaDao.insert(tarea)
    }

    // Eliminar una tarea de la base de datos
    override suspend fun deleteItem(tarea: Tarea) {
        // Elimina una tarea de la base de datos a través del DAO
        tareaDao.delete(tarea)
    }

    // Actualizar una tarea existente
    override suspend fun updateItem(tarea: Tarea) {
        // Actualiza una tarea en la base de datos a través del DAO
        tareaDao.update(tarea)
    }

    // Insertar una tarea y obtener su id
    override suspend fun insertItemAndGetId(tarea: Tarea): Long {
        // Inserta una tarea y devuelve su id generado por el DAO
        return tareaDao.insertAndGetId(tarea)
    }
}
