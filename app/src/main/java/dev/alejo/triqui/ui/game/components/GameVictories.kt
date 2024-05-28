package dev.alejo.triqui.ui.game.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alejo.triqui.R
import dev.alejo.triqui.data.network.model.GameVictories
import dev.alejo.triqui.ui.model.PlayerType
import dev.alejo.triqui.ui.theme.Black80
import dev.alejo.triqui.ui.theme.Gold40
import dev.alejo.triqui.ui.theme.Gold80
import dev.alejo.triqui.ui.theme.SmallRoundCorner
import dev.alejo.triqui.ui.theme.TransparentBlack80
import dev.alejo.triqui.ui.theme.TransparentGold10

@Composable
fun GameVictories(gameVictories: GameVictories, playerType: PlayerType) {
    if (playerType == PlayerType.Main) {
        GameVictoriesBoard(
            victories = gameVictories.mainPlayer,
            draw = gameVictories.draw,
            lost = gameVictories.secondPlayer
        )
    } else {
        GameVictoriesBoard(
            victories = gameVictories.secondPlayer,
            draw = gameVictories.draw,
            lost = gameVictories.mainPlayer
        )
    }
}

@Composable
fun GameVictoriesBoard(victories: Int, draw: Int, lost: Int) {
    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.results),
            fontSize = 18.sp,
            color = Black80
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VictoryItem(victories = victories.toString(), R.string.won)
            VerticalDivider()
            VictoryItem(victories = draw.toString(), R.string.draw)
            VerticalDivider()
            VictoryItem(victories = lost.toString(), R.string.lost)
        }
    }
}

@Composable
fun VictoryItem(victories: String, @StringRes msg: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(72.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(SmallRoundCorner))
                .background(TransparentGold10),
            contentAlignment = Alignment.Center
        ) {
            Text(text = victories, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Gold80)
        }
        Text(
            text = stringResource(id = msg),
            color = Gold40
        )
    }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(72.dp)
            .width(1.dp), thickness = 1.dp, color = TransparentBlack80
    )
}