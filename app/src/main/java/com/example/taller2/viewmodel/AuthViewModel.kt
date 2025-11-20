package com.example.taller2.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState

    /**
     * Registra un nuevo usuario y guarda su nombre de usuario.
     */
    fun register(email: String, password: String, displayName: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Guarda el nombre de usuario en el perfil de Firebase
                    val user = task.result?.user
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        _authState.value = AuthState.SignedIn
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error de registro desconocido")
                }
            }
    }

    /**
     * Inicia sesión con correo y contraseña.
     */
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.SignedIn
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error de inicio de sesión desconocido")
                }
            }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.SignedOut
    }
}

sealed interface AuthState {
    object SignedIn : AuthState
    object SignedOut : AuthState
    object Loading : AuthState
    data class Error(val message: String) : AuthState
}
