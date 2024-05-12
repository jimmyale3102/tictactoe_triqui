package dev.alejo.triqui.ui.model

import dev.alejo.triqui.data.network.model.GameData
import dev.alejo.triqui.data.network.model.PlayerData

data class GameModel(
    val gameId: String,
    val board: List<PlayerType>,
    val player1: PlayerModel,
    val player2: PlayerModel?,
    val playerTurn: PlayerModel,
    val isGameReady: Boolean = false
) {
    fun toData(): GameData = GameData(
        board = board.map { it.id },
        gameId = gameId,
        player1 = player1.toData(),
        player2 = player2?.toData(),
        playerTurn = playerTurn.toData()
    )
}

data class PlayerModel(val id: String, val playerType: PlayerType) {
    fun toData(): PlayerData = PlayerData(
        userId = id,
        playerType = playerType.id
    )
}

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