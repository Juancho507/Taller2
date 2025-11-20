package com.example.taller2.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    // Firebase authentication instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // StateFlow to track authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState

    // Function to register a new user
    fun register(email: String, password: String, displayName: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update user profile with display name
                    val user = task.result?.user
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        _authState.value = AuthState.SignedIn
                    }
                } else {
                    // If registration fails, set error state with message
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error de registro desconocido")
                }
            }
    }

    // Function to log in an existing user
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.SignedIn
                } else {
                    // If login fails, set error state with message
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error de inicio de sesión desconocido")
                }
            }
    }

    // Function to sign out the current user
    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.SignedOut
    }
}

// Sealed interface representing the authentication states
sealed interface AuthState {
    object SignedIn : AuthState
    object SignedOut : AuthState
    object Loading : AuthState
    data class Error(val message: String) : AuthState
}
