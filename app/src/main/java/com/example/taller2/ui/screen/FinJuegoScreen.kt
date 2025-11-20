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
    ganador: String,
    onVolverInicio: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0B2)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
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
                Text(
                    text = "🎉 ¡Fin del Juego! 🎉",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD84315)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "El ganador es:",
                    fontSize = 20.sp,
                    color = Color(0xFF5D4037)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ganador,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF6C00)
                )

                Spacer(modifier = Modifier.height(32.dp))

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