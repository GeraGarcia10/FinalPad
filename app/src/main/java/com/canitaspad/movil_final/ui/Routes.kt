package com.canitaspad.movil_final.ui

sealed class Routes(val route: String){
    object NotasEditor: Routes("notasEditor")
    object NotasScreen: Routes("notasScreen")
    object TareasEditor: Routes("tareasEditor")
    object TareasScreen: Routes("tareasScreen")
}

