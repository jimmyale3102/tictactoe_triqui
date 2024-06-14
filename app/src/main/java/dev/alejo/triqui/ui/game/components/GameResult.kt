package dev.alejo.triqui.ui.game.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alejo.triqui.R
import dev.alejo.triqui.ui.game.Board
import dev.alejo.triqui.ui.home.components.TriquiButton
import dev.alejo.triqui.ui.home.components.TriquiOutlinedButton
import dev.alejo.triqui.ui.model.GameModel
import dev.alejo.triqui.ui.model.PlayerType
import dev.alejo.triqui.ui.theme.Black80

@Composable
fun GameResult(
    game: GameModel,
    winner: PlayerType,
    playerType: PlayerType,
    onGoHome: () -> Unit,
    onPlayAgain: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VictoryResult(winner, playerType)
        Spacer(modifier = Modifier.height(16.dp))
        GameVictories(gameVictories = game.victories, playerType = playerType)
        Spacer(modifier = Modifier.height(16.dp))
        Board(game) {  }
        Spacer(modifier = Modifier.height(16.dp))
        PlayAgainStatus(game, playerType)
        GameOptions(onGoHome, onPlayAgain)
    }
}

@Composable
fun GameOptions(onGoHome: () -> Unit, onPlayAgain: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TriquiOutlinedButton(icon = R.drawable.ic_home, text = R.string.go_home) {
            onGoHome()
        }
        Spacer(Modifier.width(16.dp))
        TriquiButton(icon = R.drawable.ic_restart, text = R.string.play_again) {
            onPlayAgain()
        }
    }
}

@Composable
fun PlayAgainStatus(game: GameModel, playerType: PlayerType) {
    when {
        (game.mainPlayerPlayAgain && (playerType == PlayerType.Main))
            || (game.secondPlayerPlayAgain && (playerType == PlayerType.Second)) -> {
            WaitingToPlayAgain(R.string.waiting_for_your_opponent)
        }

        (game.mainPlayerPlayAgain && (playerType == PlayerType.Second))
            || (game.secondPlayerPlayAgain && (playerType == PlayerType.Main)) -> {
            WaitingToPlayAgain(R.string.opponent_wants_to_play_again)
        }
    }
}

@Composable
private fun WaitingToPlayAgain(@StringRes msg: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(msg))
    }
}

@Composable
private fun VictoryResult(winner: PlayerType, playerType: PlayerType) {
    val gameResult = if (winner == PlayerType.Empty) {
        stringResource(R.string.draw)
    } else {
        if (winner == playerType) {
            stringResource(R.string.you_won)
        } else {
            stringResource(R.string.you_lost)
        }
    }
    Text(
        modifier = Modifier.padding(16.dp),
        text = gameResult,
        fontSize = 28.sp,
        color = Black80,
        fontWeight = FontWeight.Bold
    )
}