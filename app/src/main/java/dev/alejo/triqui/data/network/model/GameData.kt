package dev.alejo.triqui.data.network.model

import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerModel
import dev.alejo.triqui.ui.model.PlayerType
import java.util.Calendar

data class GameData(
    val board: List<Int>? = null,
    val gameId: String? = null,
    val player1: PlayerData? = null,
    val player2: PlayerData? = null,
    val playerTurn: PlayerData? = null,
    val player1PlayAgain: Boolean = false,
    val player2PlayAgain: Boolean = false
)

fun GameData.toModel(): GameModel = GameModel(
    board = board?.map { playerId -> PlayerType.getPlayerTypeById(playerId) } ?: mutableListOf(),
    gameId = gameId.orEmpty(),
    player1 = player1!!.toModel(),
    player2 = player2?.toModel(),
    playerTurn = playerTurn!!.toModel(),
    player1PlayAgain = player1PlayAgain,
    player2PlayAgain = player2PlayAgain
)

data class PlayerData(
    val userId: String? = Calendar.getInstance().timeInMillis.hashCode().toString(),
    val playerType: Int? = null
)

fun PlayerData.toModel(): PlayerModel = PlayerModel(
    id = userId!!,
    playerType = PlayerType.getPlayerTypeById(playerType)
)