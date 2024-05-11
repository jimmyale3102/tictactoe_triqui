package dev.alejo.triqui.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String,
    playerId: String,
    isOwner: Boolean
) {
    Board()
}

@Composable
fun Board() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "This is the game Id")
        Text(text = "Your turn / Opponent turn / Waiting for opponent")

        Row {
            GameItem()
            GameItem()
            GameItem()
        }
        Row {
            GameItem()
            GameItem()
            GameItem()
        }
        Row {
            GameItem()
            GameItem()
            GameItem()
        }
    }
}

@Composable
fun GameItem() {
    Box(
        Modifier
            .size(64.dp)
            .padding(4.dp)
            .border(BorderStroke(1.dp, Color.Black)), contentAlignment = Alignment.Center
    ) {
        Text("X")
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun GameScreenPreview() {
    GameScreen()
}