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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller2.viewmodel.AuthViewModel
import com.example.taller2.viewmodel.AuthState

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onVolverInicio: () -> Unit
) {
    // Collect authentication state from the ViewModel
    val authState by authViewModel.authState.collectAsState()

    // Local state to store the email and password entered by the user
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Local state for showing error messages
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // React to authentication state changes (success or error)
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.SignedIn -> onLoginSuccess() // Navigate on successful login
            is AuthState.Error -> errorMessage = state.message // Display the error message
            else -> errorMessage = null
        }
    }

    // Main container with a vertical gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1174CB), Color(0xFFC4D0FA))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Card for the login form UI
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title text
                Text(
                    "Iniciar Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Password field with hidden characters
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Show loading indicator while signing in
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator()
                } else {
                    // Login button
                    Button(
                        onClick = { authViewModel.login(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Entrar", fontSize = 18.sp)
                    }
                }

                // Display error messages if any
                errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                // Back to home button
                TextButton(onClick = onVolverInicio) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}
