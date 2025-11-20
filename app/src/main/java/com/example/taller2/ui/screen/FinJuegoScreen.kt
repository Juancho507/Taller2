package com.example.taller2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FinJuegoScreen(
    ganador: String,               // Name or identifier of the player who won the game
    onVolverInicio: () -> Unit     // Callback invoked when the user returns to the home screen
) {

    // Root container that centers everything and applies a background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0B2)),
        contentAlignment = Alignment.Center
    ) {

        // Card that displays the winner info and the button
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            // Vertical layout inside the card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title: "Game Over"
                Text(
                    text = "🎉 ¡Fin del Juego! 🎉",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD84315)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Small subtitle
                Text(
                    text = "El ganador es:",
                    fontSize = 20.sp,
                    color = Color(0xFF5D4037)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Highlighted winner name
                Text(
                    text = ganador,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF6C00)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Button to return to the home screen
                Button(
                    onClick = { onVolverInicio() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315)),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(
                        text = "Volver al Inicio",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
