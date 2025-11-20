package com.example.taller22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.ui.screen.*
import com.example.taller2.ui.theme.EmojiGuessTheme
import com.example.taller2.viewmodel.AuthViewModel
import com.example.taller2.viewmodel.GameViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmojiGuessTheme {
                val authViewModel: AuthViewModel = viewModel()
                val gameViewModel: GameViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("inicio") }
                var gameId by remember { mutableStateOf<String?>(null) }

                when (currentScreen) {
                    "inicio" -> InicioScreen(
                        onRegistroClick = { currentScreen = "registro" },
                        onLoginClick = { currentScreen = "login" } // Navigate to login screen
                    )

                    "registro" -> RegistroScreen(
                        onRegistroExitoso = { currentScreen = "unirse" }, // Navigate to join/create room screen after successful registration
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )

                    "login" -> LoginScreen(
                        authViewModel = authViewModel,
                        onLoginSuccess = { currentScreen = "unirse" }, // Navigate to join/create room screen after successful login
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )

                    "unirse" -> UnirseScreen(
                        gameViewModel = gameViewModel,
                        onUnirseExitoso = { newGameId ->
                            gameId = newGameId
                            currentScreen = "sala" // Navigate to the game room screen
                        },
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )

                    "sala" -> SalaScreen(
                        gameId = gameId ?: "", // Pass the current game ID to the room screen
                        gameViewModel = gameViewModel,
                        onJugarClick = { currentScreen = "juego" }, // Navigate to the game screen when the game starts
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )

                    "juego" -> JuegoScreen(
                        gameViewModel = gameViewModel,
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )

                    "fin" -> FinJuegoScreen(
                        ganador = "Jugador 1",
                        onVolverInicio = { currentScreen = "inicio" } // Go back to start screen
                    )
                }
            }
        }
    }
}
