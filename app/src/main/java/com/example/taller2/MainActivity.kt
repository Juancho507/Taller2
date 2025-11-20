package com.example.taller2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.ui.screen.*
import com.example.taller2.ui.theme.EmojiGuessTheme
import com.example.taller2.viewmodel.AuthViewModel
import com.example.taller2.viewmodel.GameViewModel
import com.example.taller2.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmojiGuessTheme {
                // --- VIEWMODELS CENTRALIZADOS Y CON FÁBRICA ---
                val authViewModel: AuthViewModel = viewModel()
                val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory()) // Usamos la fábrica

                var currentScreen by remember { mutableStateOf("inicio") }
                var gameId by remember { mutableStateOf<String?>(null) }

                when (currentScreen) {
                    "inicio" -> InicioScreen(
                        onRegistroClick = { currentScreen = "registro" },
                        onLoginClick = { currentScreen = "login" }
                    )

                    "registro" -> RegistroScreen(
                        authViewModel = authViewModel,
                        onRegistroExitoso = { currentScreen = "unirse" },
                        onVolverInicio = { currentScreen = "inicio" }
                    )

                    "login" -> LoginScreen(
                        authViewModel = authViewModel,
                        onLoginSuccess = { currentScreen = "unirse" },
                        onVolverInicio = { currentScreen = "inicio" }
                    )

                    "unirse" -> UnirseScreen(
                        gameViewModel = gameViewModel,
                        onUnirseExitoso = { newGameId ->
                            gameId = newGameId
                            currentScreen = "sala"
                        },
                        onVolverInicio = { currentScreen = "inicio" }
                    )

                    "sala" -> SalaScreen(
                        gameId = gameId ?: "",
                        gameViewModel = gameViewModel,
                        onJugarClick = { currentScreen = "juego" },
                        onVolverInicio = { currentScreen = "inicio" }
                    )

                    "juego" -> JuegoScreen(
                        gameViewModel = gameViewModel,
                        onVolverInicio = { currentScreen = "inicio" }
                    )

                    "fin" -> FinJuegoScreen(
                        ganador = "Jugador 1",
                        onVolverInicio = { currentScreen = "inicio" }
                    )
                }
            }
        }
    }
}
