package dev.alejo.triqui.ui.model

data class GameModel(
    val gameId: String,
    val board: List<PlayerType>,
    val player1: PlayerModel,
    val player2: PlayerModel?,
    val playerTurn: PlayerModel
)

data class PlayerModel(val id: String, val playerType: PlayerType)

sealed class PlayerType(val id: Int, val symbol: String) {
    data object PlayerOwner : PlayerType(2, "X")
    data object PlayerGuest : PlayerType(3, "O")
    data object Empty : PlayerType(4, "")

    companion object {
        fun getPlayerTypeById(playerId: Int?): PlayerType = when (playerId) {
            PlayerOwner.id -> PlayerOwner
            PlayerGuest.id -> PlayerGuest
            else -> Empty
        }
    }
}