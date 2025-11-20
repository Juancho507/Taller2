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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.viewmodel.AuthViewModel
import com.example.taller2.viewmodel.AuthState

@Composable
fun RegistroScreen(
    authViewModel: AuthViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onVolverInicio: () -> Unit
) {
    // Collect authentication state from ViewModel
    val authState by authViewModel.authState.collectAsState()

    // Local state for user inputs
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Error message state
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // React to authentication state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.SignedIn -> onRegistroExitoso() // Navigate when registration completes successfully
            is AuthState.Error -> errorMessage = state.message // Show error message
            else -> errorMessage = null
        }
    }

    // Background gradient for the screen
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFAC43D), Color(0xFFFFED64))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        // Main card for form UI
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title text
                Text(
                    "Crear Cuenta",
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFFC107)
                )

                // Username input field
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Email input field
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Password input field
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Loading indicator while registering
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator()
                } else {
                    // Register button
                    Button(
                        onClick = {
                            // Validate input before sending registration request
                            if (nombre.isNotBlank() && correo.isNotBlank() && contrasena.isNotBlank()) {
                                authViewModel.register(correo, contrasena, nombre)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                    ) {
                        Text(
                            "Registrar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Display error messages from ViewModel
                errorMessage?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                // Return to home screen button
                TextButton(onClick = { onVolverInicio() }) {
                    Text("⬅ Volver al inicio", color = Color(0xFFFF5722))
                }
            }
        }
    }
}
