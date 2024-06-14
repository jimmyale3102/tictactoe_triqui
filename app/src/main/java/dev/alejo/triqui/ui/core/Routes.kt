package dev.alejo.triqui.ui.core

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    object Game : Routes("game/{gameId}/{playerId}/{isOwner}/{isSinglePlayer}") {
        fun createRoute(gameId: String, playerId: String, isOwner: Boolean, isSinglePlayer: Boolean): String {
            return "game/$gameId/$playerId/$isOwner/$isSinglePlayer"
        }
    }
}