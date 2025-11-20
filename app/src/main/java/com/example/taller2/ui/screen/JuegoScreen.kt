package com.example.taller2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.models.GameRoom
import com.example.taller2.viewmodel.GameViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun JuegoScreen(
    gameViewModel: GameViewModel = viewModel(), // Main ViewModel that manages the game state
    onVolverInicio: () -> Unit                 // Callback to exit the match
) {
    // Observes the GameRoom object from the ViewModel
    val gameRoom by gameViewModel.gameRoom.collectAsState()

    // Gets the current player's UID
    val myUid = FirebaseAuth.getInstance().currentUser?.uid

    // State for countdown timer UI
    var timeLeft by remember { mutableStateOf(30) }

    // ⏳ Coroutine that updates the timer until the round ends
    LaunchedEffect(gameRoom?.roundEndTime) {
        gameRoom?.let {
            while (System.currentTimeMillis() < it.roundEndTime) {
                val remaining = (it.roundEndTime - System.currentTimeMillis()) / 1000
                timeLeft = remaining.toInt()
                delay(1000)
            }
            timeLeft = 0
        }
    }

    // Background gradient for the game screen
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
    )

    // Main column layout of the game screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStatusHeader(gameRoom = gameRoom, timeLeft = timeLeft)

        Spacer(modifier = Modifier.height(16.dp))

        PlayerList(gameRoom = gameRoom, myUid = myUid)

        Spacer(modifier = Modifier.height(16.dp))

        EmojiGuessingGrid(gameViewModel = gameViewModel)

        Spacer(modifier = Modifier.weight(1f))

        // Button to exit the match
        Button(onClick = onVolverInicio) {
            Text("Salir de la Partida")
        }
    }
}

@Composable
private fun GameStatusHeader(gameRoom: GameRoom?, timeLeft: Int) {

    // Shows either winner or current round number
    val statusText = when {
        gameRoom?.winnerId != null ->
            "🏆 Ganador: ${gameRoom.players[gameRoom.winnerId]?.name}"

        else ->
            "Ronda ${gameRoom?.roundNumber ?: 1}"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shows round or winner
        Text(
            text = statusText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Shows the countdown timer
        Text(
            text = "⏰ $timeLeft s",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun PlayerList(gameRoom: GameRoom?, myUid: String?) {

    // Iterates through all players in the room
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        gameRoom?.players?.values?.forEach { player ->

            // Players see only a "❓" for their own emoji
            val emoji = if (player.uid == myUid) "❓" else player.emoji

            PlayerRow(
                name = player.name,
                emoji = emoji,
                isEliminated = player.isEliminated
            )
        }
    }
}

@Composable
private fun PlayerRow(name: String, emoji: String, isEliminated: Boolean) {

    // Eliminated players appear greyed out
    val backgroundColor =
        if (isEliminated) Color.Gray.copy(alpha = 0.5f)
        else Color.White.copy(alpha = 0.3f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Player name
        Text(
            name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        // Emoji or placeholder
        Text(emoji, fontSize = 24.sp)
    }
}

@Composable
private fun EmojiGuessingGrid(gameViewModel: GameViewModel) {

    // List of selectable emojis from GameLogic
    val availableEmojis = com.example.taller2.models.GameLogic.EMOJIS

    Text(
        "🤔 ¿Cuál es tu emoji?",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Grid of emojis for guessing
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 60.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        items(availableEmojis) { emoji ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(60.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .clickable {
                        // Sends the guessed emoji to the ViewModel
                        gameViewModel.submitGuess(emoji)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 30.sp)
            }
        }
    }
}
