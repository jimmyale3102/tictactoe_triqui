package dev.alejo.triqui.ui.core

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Game : Routes("game")
}