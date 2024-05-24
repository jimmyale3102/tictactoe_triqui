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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alejo.triqui.R
import dev.alejo.triqui.ui.theme.Black80
import dev.alejo.triqui.ui.theme.GeneralRoundCorner
import dev.alejo.triqui.ui.theme.Gold40
import dev.alejo.triqui.ui.theme.Gold80
import dev.alejo.triqui.ui.theme.TransparentBlack80
import dev.alejo.triqui.ui.theme.TransparentGold10

@Composable
fun GameVictories(victories: Int, draw: Int, lost: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.victories),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Black80
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VictoryItem(victories = victories.toString(), R.string.you)
            VerticalDivider()
            VictoryItem(victories = draw.toString(), R.string.draw)
            VerticalDivider()
            VictoryItem(victories = lost.toString(), R.string.opponent)
        }
    }
}

@Composable
fun VictoryItem(victories: String, @StringRes msg: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(92.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(GeneralRoundCorner))
                .background(TransparentGold10),
            contentAlignment = Alignment.Center
        ) {
            Text(text = victories, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Gold80)
        }
        Text(
            text = stringResource(id = msg),
            fontWeight = FontWeight.Bold,
            color = Gold40
        )
    }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(92.dp)
            .width(1.dp), thickness = 1.dp, color = TransparentBlack80
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GameVictoriesPreview() {
    GameVictories(
        victories = 3,
        draw = 6,
        lost = 2
    )
}