package com.canitaspad.movil_final

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.canitaspad.movil_final.viewModel.NotaDetailsViewModel
import com.canitaspad.movil_final.viewModel.NotasEditorViewModel
import com.canitaspad.movil_final.viewModel.NotasScreenViewModel
import com.canitaspad.movil_final.viewModel.TareaDetailsViewModel
import com.canitaspad.movil_final.viewModel.TareasEditorViewModel
import com.canitaspad.movil_final.viewModel.TareasScreenViewModel
import com.canitaspad.movil_final.viewModel.UpdateNotaViewModel
import com.canitaspad.movil_final.viewModel.UpdateTareaViewModel

object AppViewModelProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        // Inicialización de los ViewModels con sus respectivas dependencias

        initializer {
            // Editor de Notas
            NotasEditorViewModel(
                notasApplication().container.notasRepository,
                notasApplication().container.notasMultimediaRepository
            )
        }

        initializer {
            // Editor de Tareas
            TareasEditorViewModel(
                notasApplication().container.tareasRepository,
                notasApplication().container.tareasMultimediaRepository
            )
        }

        initializer {
            // Pantalla principal de Notas
            NotasScreenViewModel(notasApplication().container.notasRepository)
        }

        initializer {
            // Pantalla principal de Tareas
            TareasScreenViewModel(notasApplication().container.tareasRepository)
        }

        initializer {
            // Detalles de Nota
            NotaDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository
            )
        }

        initializer {
            // Actualización de Nota
            UpdateNotaViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository,
                notasApplication().container.notasMultimediaRepository
            )
        }

        initializer {
            // Detalles de Tarea
            TareaDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository
            )
        }

        initializer {
            // Actualización de Tarea
            UpdateTareaViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository,
                notasApplication().container.tareasMultimediaRepository
            )
        }
    }
}

// Función de extensión para obtener la instancia de NotasApplication desde CreationExtras
fun CreationExtras.notasApplication(): NotasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NotasApplication)
