package dev.alejo.triqui.ui.game

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alejo.triqui.R
import dev.alejo.triqui.ui.game.components.GameResult
import dev.alejo.triqui.ui.game.components.GameVictories
import dev.alejo.triqui.ui.home.components.TriquiButton
import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerType
import dev.alejo.triqui.ui.theme.Blue40
import dev.alejo.triqui.ui.theme.GeneralRoundCorner
import dev.alejo.triqui.ui.theme.Gold80
import dev.alejo.triqui.ui.theme.TransparentBlack80
import dev.alejo.triqui.ui.theme.TransparentGold10

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String,
    playerId: String,
    isOwner: Boolean,
    isSinglePlayer: Boolean,
    onGoHome: () -> Unit
) {
    LaunchedEffect(true) {
        gameViewModel.joinToGame(gameId, playerId, isOwner, isSinglePlayer)
    }

    val game: GameModel? by gameViewModel.game.collectAsState()
    val winner: PlayerType? by gameViewModel.winner.collectAsState()
    val playerType = game?.let {
        if (it.isGameReady) {
            gameViewModel.getPlayer()
        } else {
            PlayerType.Main
        }
    } ?: PlayerType.Main

    if (winner != null) {
        GameResult(
            game = game!!,
            winner = winner!!,
            playerType = playerType,
            onGoHome = { onGoHome() },
            onPlayAgain = { gameViewModel.onPlayAgain() }
        )
    } else {
        val context = LocalContext.current
        Game(
            game = game,
            playerType = playerType,
            shareGameId = {
                shareGameId(context, game?.gameId)
            }
        ) { position ->
            gameViewModel.updateGame(position)
        }
    }
}

private fun shareGameId(context: Context, gameId: String?) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, gameId)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun Game(
    game: GameModel?,
    playerType: PlayerType,
    shareGameId: () -> Unit,
    onPressed: (Int) -> Unit
) {
    if (game == null) return
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(0.5f))
        GameStatus(game)
        Spacer(modifier = Modifier.weight(1f))
        GameVictories(gameVictories = game.victories, playerType = playerType)
        Spacer(modifier = Modifier.weight(1f))
        Board(game) { position -> onPressed(position) }
        Spacer(modifier = Modifier.weight(1f))
        if (playerType == PlayerType.Main && !game.singlePlayer) {
            ShareGame { shareGameId() }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun GameStatus(game: GameModel) {
    if (game.isGameReady) {
        val gameTurn = if (game.isMyTurn) {
            stringResource(R.string.your_turn)
        } else {
            stringResource(R.string.opponent_turn)
        }
        Text(text = gameTurn)
    } else {
        if (!game.singlePlayer) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = stringResource(R.string.waiting_for_your_opponent))
            }
        }
    }
}

@Composable
fun Board(game: GameModel, onPressed: (Int) -> Unit) {
    Row {
        GameItem(game.board[0]) { onPressed(0) }
        BoardVerticalDivider()
        GameItem(game.board[1]) { onPressed(1) }
        BoardVerticalDivider()
        GameItem(game.board[2]) { onPressed(2) }
    }
    BoardHorizontalDivider()
    Row {
        GameItem(game.board[3]) { onPressed(3) }
        BoardVerticalDivider()
        GameItem(game.board[4]) { onPressed(4) }
        BoardVerticalDivider()
        GameItem(game.board[5]) { onPressed(5) }
    }
    BoardHorizontalDivider()
    Row {
        GameItem(game.board[6]) { onPressed(6) }
        BoardVerticalDivider()
        GameItem(game.board[7]) { onPressed(7) }
        BoardVerticalDivider()
        GameItem(game.board[8]) { onPressed(8) }
    }
}

@Composable
private fun BoardVerticalDivider() {
    Divider(
        modifier = Modifier
            .height(92.dp)
            .width(1.dp), thickness = 1.dp, color = TransparentBlack80
    )
}

@Composable
private fun BoardHorizontalDivider() {
    Divider(
        modifier = Modifier
            .height(1.dp)
            .width(276.dp), thickness = 1.dp, color = TransparentBlack80
    )
}

@Composable
fun GameItem(playerType: PlayerType, onPressed: () -> Unit) {
    Box(
        Modifier
            .size(92.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(GeneralRoundCorner))
            .background(TransparentGold10)
            .clickable { onPressed() },
        contentAlignment = Alignment.Center
    ) {
        val playerColor = if (playerType is PlayerType.Main) Gold80 else Blue40
        Text(playerType.symbol, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = playerColor)
    }
}

@Composable
fun ShareGame(shareGameId: () -> Unit) {
    TriquiButton(
        icon = R.drawable.ic_share,
        text = R.string.share_game_id
    ) {
        shareGameId()
    }
}