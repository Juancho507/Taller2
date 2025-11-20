package com.example.taller2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen(
    onRegistroClick: () -> Unit, // Callback triggered when the "Register" button is clicked
    onLoginClick: () -> Unit     // Callback triggered when the "Login" button is clicked
) {

    // Main container that fills the screen and displays a vertical gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF636BFA), Color(0xFFC4D0FA)) // Blue-to-light gradient
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Column that centers all the content vertically
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp) // Spacing between items
        ) {

            // App title
            Text(
                text = "Emoji Guess",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // "Register" button
            Button(
                onClick = onRegistroClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E00FD),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // "Login" button
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1174CB),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
