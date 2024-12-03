package com.canitaspad.movil_final.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.canitaspad.movil_final.model.Nota
import com.canitaspad.movil_final.model.NotaMultimedia

@Database(entities = [Nota::class, NotaMultimedia::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotaDao(): NotaDAO
    abstract fun getNotaMultimediaDao(): NotaMultimediaDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "notas_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
