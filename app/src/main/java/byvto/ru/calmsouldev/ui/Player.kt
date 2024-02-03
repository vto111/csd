package byvto.ru.calmsouldev.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import byvto.ru.calmsouldev.PlayerState
import byvto.ru.calmsouldev.R

@Composable
fun Player(
    state: PlayerState,
    playPauseToggle: () -> Unit,
    hideClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.tollev_white_v2),
                    contentDescription = "head",
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = state.id.toString(),
//                    text = track.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = { playPauseToggle() },
                shape = CircleShape,
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    painter = if (state.isPlaying) {
                        painterResource(R.drawable.ic_pause)
                    } else {
                        painterResource(R.drawable.ic_play)
                    },
                    contentDescription = "play",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { hideClicked() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hide),
                    contentDescription = "hide",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}