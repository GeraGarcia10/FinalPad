package com.canitaspad.movil_final.data

import android.content.Context

interface AppContainer {
    val notasRepository: NotasRepository
    val notasMultimediaRepository: NotaMultimediaRepository
    val tareasMultimediaRepository: TareaMultimediaRepository
    val tareasRepository: TareasRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    // Almacenamos las bases de datos para evitar repetición de getDatabase
    private val notasDatabase = NotasDatabase.getDatabase(context)
    private val tareasDatabase = TareasDatabase.getDatabase(context)

    override val notasRepository: NotasRepository by lazy {
        OfflineNotasRepository(notasDatabase.notaDao())
    }

    override val notasMultimediaRepository: NotaMultimediaRepository by lazy {
        OfflineNotaMultimediaRepository(notasDatabase.notaMultimediaDao())
    }

    override val tareasRepository: TareasRepository by lazy {
        OfflineTareasRepository(tareasDatabase.tareaDao())
    }

    override val tareasMultimediaRepository: TareaMultimediaRepository by lazy {
        OfflineTareaMultimediaRepository(tareasDatabase.tareaMultimediaDao())
    }
}
