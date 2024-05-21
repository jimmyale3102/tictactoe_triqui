package dev.alejo.triqui.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alejo.triqui.R
import dev.alejo.triqui.ui.home.components.TriquiButton
import dev.alejo.triqui.ui.theme.Black80

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGame: (String, String, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Header()
        Spacer(modifier = Modifier.weight(2f))
        CreateGame(onCreateGame = { homeViewModel.onCreateGame(navigateToGame) })
        DividerSection(modifier = Modifier.padding(vertical = 32.dp))
        JoinGame(onJoinGame = { gameId -> homeViewModel.onJoinGame(gameId, navigateToGame) })
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black80
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(
                    id = R.string.app_name
                )
            )
        }
    }
}

@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 8.dp)
                .weight(1f)
        )
        Text(text = stringResource(R.string.or))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 8.dp)
                .weight(1f)
        )
    }
}

@Composable
fun CreateGame(onCreateGame: () -> Unit) {
    TriquiButton(
        modifier = Modifier.padding(8.dp),
        icon = R.drawable.ic_game,
        text = R.string.create_game
    ) { onCreateGame() }
}

@Composable
fun JoinGame(onJoinGame: (String) -> Unit) {
    var gameId by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.clip(RoundedCornerShape(24.dp)),
        value = gameId,
        onValueChange = { gameId = it },
        placeholder = { Text(text = stringResource(R.string.type_a_game_id)) },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        singleLine = true
    )
    TriquiButton(
        modifier = Modifier.padding(8.dp),
        icon = R.drawable.ic_join,
        text = R.string.join_game,
        enabled = gameId.isNotEmpty()
    ) { onJoinGame(gameId) }

}