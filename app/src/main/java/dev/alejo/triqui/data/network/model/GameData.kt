package dev.alejo.triqui.data.network.model

import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerModel
import dev.alejo.triqui.ui.model.PlayerType
import java.util.Calendar

data class GameData(
    val date: String = Calendar.getInstance().time.toString(),
    val board: List<Int>? = null,
    val gameId: String? = null,
    val mainPlayer: PlayerData? = null,
    val secondPlayer: PlayerData? = null,
    val playerTurn: PlayerData? = null,
    val victories: GameVictories? = null,
    val mainPlayerPlayAgain: Boolean = false,
    val secondPlayerPlayAgain: Boolean = false,
    val isSinglePlayer: Boolean = false
)

fun GameData.toModel(): GameModel = GameModel(
    board = board?.map { playerId -> PlayerType.getPlayerTypeById(playerId) } ?: mutableListOf(),
    gameId = gameId.orEmpty(),
    mainPlayer = mainPlayer!!.toModel(),
    secondPlayer = secondPlayer?.toModel(),
    playerTurn = playerTurn!!.toModel(),
    victories = victories ?: GameVictories(),
    mainPlayerPlayAgain = mainPlayerPlayAgain,
    secondPlayerPlayAgain = secondPlayerPlayAgain,
    isSinglePlayer = isSinglePlayer
)

data class PlayerData(
    val userId: String? = Calendar.getInstance().timeInMillis.hashCode().toString(),
    val playerType: Int? = null
)
data class GameVictories(val mainPlayer: Int = 0, val secondPlayer: Int = 0, val draw: Int = 0)

fun PlayerData.toModel(): PlayerModel = PlayerModel(
    id = userId!!,
    playerType = PlayerType.getPlayerTypeById(playerType)
)
