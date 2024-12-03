package com.canitaspad.movil_final.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.canitaspad.movil_final.model.Tarea
import com.canitaspad.movil_final.model.TareaMultimedia

@Database(entities = [Tarea::class, TareaMultimedia::class], version = 10, exportSchema = false)
abstract class TareasDatabase : RoomDatabase() {

    // Función abstracta para acceder al DAO de Tarea
    abstract fun tareaDao(): TareaDAO

    // Función abstracta para acceder al DAO de Tarea Multimedia
    abstract fun tareaMultimediaDao(): TareaMultimediaDAO

    companion object {
        // Instancia única de la base de datos
        @Volatile
        private var instance: TareasDatabase? = null

        // Método para obtener la instancia de la base de datos (Singleton)
        fun getDatabase(context: Context): TareasDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, TareasDatabase::class.java, "tarea_database")
                    .fallbackToDestructiveMigration() // Permite la eliminación de datos si la migración falla
                    .build()
                    .also { instance = it }
            }
        }
    }
}
