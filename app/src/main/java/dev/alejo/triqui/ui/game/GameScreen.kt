package dev.alejo.triqui.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerType

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String,
    playerId: String,
    isOwner: Boolean
) {
    LaunchedEffect(true) {
        gameViewModel.joinToGame(gameId, playerId, isOwner)
    }

    val game: GameModel? by gameViewModel.game.collectAsState()
    Board(game) { position ->
        gameViewModel.updateGame(position)
    }
}

@Composable
fun Board(game: GameModel?, onPressed: (Int) -> Unit) {
    if (game == null) return
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = game.gameId.orEmpty())
        val gameStatus = if (game.isGameReady) {
            if (game.isMyTurn) {
                "My turn"
            } else {
                "Opponent turn"
            }
        } else {
            "Waiting for the opponent"
        }
        Text(text = gameStatus)

        Row {
            GameItem(game.board[0]) { onPressed(0) }
            GameItem(game.board[1]) { onPressed(1) }
            GameItem(game.board[2]) { onPressed(2) }
        }
        Row {
            GameItem(game.board[3]) { onPressed(3) }
            GameItem(game.board[4]) { onPressed(4) }
            GameItem(game.board[5]) { onPressed(5) }
        }
        Row {
            GameItem(game.board[6]) { onPressed(6) }
            GameItem(game.board[7]) { onPressed(7) }
            GameItem(game.board[8]) { onPressed(8) }
        }
    }
}

@Composable
fun GameItem(playerType: PlayerType, onPressed: () -> Unit) {
    Box(
        Modifier
            .size(64.dp)
            .padding(4.dp)
            .border(BorderStroke(1.dp, Color.Black))
            .clickable { onPressed() },
        contentAlignment = Alignment.Center
    ) {
        Text(playerType.symbol)
    }
}