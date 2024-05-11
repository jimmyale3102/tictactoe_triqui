package dev.alejo.triqui.ui.core

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Game : Routes("game") {
        fun createRoute(gameId: String, playerId: String, isOwner: Boolean): String {
            return "game/$gameId/$playerId/$isOwner"
        }
    }
}