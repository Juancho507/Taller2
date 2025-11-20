package com.example.taller2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.viewmodel.GameViewModel

@Composable
fun UnirseScreen(
    onUnirseExitoso: (String) -> Unit,
    onVolverInicio: () -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3FF348), Color(0xFFA0F59C))
    )

    var codigoSala by remember { mutableStateOf("") }
    var creandoSala by remember { mutableStateOf(false) }
    val gameId by gameViewModel.gameId.collectAsState()

    // Navega a la sala cuando el gameId cambia
    LaunchedEffect(gameId) {
        gameId?.let { onUnirseExitoso(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    if (creandoSala) "Crear Nueva Sala 🎮" else "Unirse a una Sala 🔑",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2d3436)
                )

                if (!creandoSala) {
                    OutlinedTextField(
                        value = codigoSala,
                        onValueChange = { codigoSala = it },
                        label = { Text("Código de la sala") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        if (creandoSala) {
                            gameViewModel.createGame()
                        } else {
                            if (codigoSala.isNotBlank()) {
                                gameViewModel.joinGame(codigoSala)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6DE372))
                ) {
                    Text(
                        if (creandoSala) "Crear Sala" else "Unirse",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(onClick = { creandoSala = !creandoSala }) {
                    Text(
                        if (creandoSala) "🔑 Ya tengo una sala" else "➕ Crear nueva sala",
                        color = Color(0xFF037207)
                    )
                }

                TextButton(onClick = { onVolverInicio() }) {
                    Text("⬅ Volver al inicio", color = Color(0xFFd63031))
                }
            }
        }
    }
}
