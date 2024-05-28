package dev.alejo.triqui.ui.home.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.alejo.triqui.R
import dev.alejo.triqui.ui.theme.GeneralRoundCorner
import dev.alejo.triqui.ui.theme.Gold40

@Composable
fun TriquiButton(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(GeneralRoundCorner),
        onClick = { onClick() },
        enabled = enabled
    ) {
        TriquiButtonContent(icon, text)
    }
}

@Composable
fun TriquiOutlinedButton(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        shape = RoundedCornerShape(GeneralRoundCorner),
        border = BorderStroke(width = 1.dp, color = Gold40),
        onClick = { onClick() },
        enabled = enabled
    ) {
        TriquiButtonContent(icon, text)
    }
}

@Composable
private fun TriquiButtonContent(@DrawableRes icon: Int, @StringRes text: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(
                id = text
            )
        )
        Text(text = stringResource(id = text))
    }
}

@Preview
@Composable
fun PreviewButton() {
    TriquiOutlinedButton(icon = R.drawable.ic_join, text = R.string.draw) { }
}