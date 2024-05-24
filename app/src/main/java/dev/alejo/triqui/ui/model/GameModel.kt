package dev.alejo.triqui.ui.model

import dev.alejo.triqui.data.network.model.GameData
import dev.alejo.triqui.data.network.model.GameVictories
import dev.alejo.triqui.data.network.model.PlayerData

data class GameModel(
    val gameId: String,
    val board: List<PlayerType>,
    val mainPlayer: PlayerModel,
    val secondPlayer: PlayerModel?,
    val playerTurn: PlayerModel,
    val isGameReady: Boolean = false,
    val isMyTurn: Boolean = false,
    val victories: GameVictories?,
    val mainPlayerPlayAgain: Boolean = false,
    val secondPlayerPlayAgain: Boolean = false
) {
    fun toData(): GameData = GameData(
        board = board.map { it.id },
        gameId = gameId,
        mainPlayer = mainPlayer.toData(),
        secondPlayer = secondPlayer?.toData(),
        playerTurn = playerTurn.toData(),
        victories = victories,
        mainPlayerPlayAgain = mainPlayerPlayAgain,
        secondPlayerPlayAgain = secondPlayerPlayAgain
    )
}

data class PlayerModel(val id: String, val playerType: PlayerType) {
    fun toData(): PlayerData = PlayerData(
        userId = id,
        playerType = playerType.id
    )
}

sealed class PlayerType(val id: Int, val symbol: String) {
    data object Main : PlayerType(1, "X")
    data object Second : PlayerType(2, "O")
    data object Empty : PlayerType(0, "")

    companion object {
        fun getPlayerTypeById(playerId: Int?): PlayerType = when (playerId) {
            Main.id -> Main
            Second.id -> Second
            else -> Empty
        }
    }
}