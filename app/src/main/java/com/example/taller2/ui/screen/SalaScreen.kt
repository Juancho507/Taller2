package com.example.taller2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.models.Player
import com.example.taller2.viewmodel.GameViewModel

@Composable
fun SalaScreen(
    gameId: String,
    onJugarClick: () -> Unit,
    onVolverInicio: () -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    // Join the game room when this screen is opened
    LaunchedEffect(gameId) {
        gameViewModel.joinGame(gameId)
    }

    // Leave the game when the screen is removed from the composition
    DisposableEffect(Unit) {
        onDispose {
            gameViewModel.leaveGame()
        }
    }

    // Observe the game room state from the ViewModel
    val gameRoom by gameViewModel.gameRoom.collectAsState()

    // Convert the HashMap of players to a list (or empty list if null)
    val players = gameRoom?.players?.values?.toList() ?: emptyList()

    // --- REACTIVE NAVIGATION ---
    // Navigate to the game screen as soon as the game starts
    LaunchedEffect(gameRoom?.isGameStarted) {
        if (gameRoom?.isGameStarted == true) {
            onJugarClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        // Main card containing the lobby UI
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title and Room Code
                Text(
                    "Sala de Juego",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Código: $gameId",
                    fontSize = 18.sp,
                    color = Color(0xFF0D47A1)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Players section label
                Text(
                    "Jugadores conectados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // List of connected players
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(2.dp, Color(0xFF90CAF9), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    items(players) { player: Player ->
                        Text(
                            "🎮 ${player.name}",
                            fontSize = 18.sp,
                            color = Color(0xFF1E88E5),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // This button triggers the game start — it does NOT navigate
                Button(
                    onClick = { gameViewModel.startGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.fillMaxWidth(0.7f),
                    enabled = players.size > 1 // Disable if fewer than 2 players
                ) {
                    Text("Comenzar Juego", fontSize = 18.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Button to return to home screen
                OutlinedButton(
                    onClick = { onVolverInicio() },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1))
                ) {
                    Text("Salir de la Sala", fontSize = 16.sp)
                }
            }
        }
    }
}
