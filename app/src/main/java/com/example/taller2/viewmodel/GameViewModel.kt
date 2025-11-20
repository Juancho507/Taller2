package com.example.taller2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taller2.data.FirebaseService
import com.example.taller2.models.GameRoom
import com.example.taller2.models.Message
import com.example.taller2.models.Player
import com.example.taller2.models.GameLogic
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val firebaseService = FirebaseService()
    private val auth = FirebaseAuth.getInstance()

    private val _gameRoom = MutableStateFlow<GameRoom?>(null)
    val gameRoom: StateFlow<GameRoom?> = _gameRoom.asStateFlow()

    private val _gameId = MutableStateFlow<String?>(null)
    val gameId: StateFlow<String?> = _gameId.asStateFlow()

    // --------------------------------------------------------------------
    // Create a new game
    // --------------------------------------------------------------------

    fun createGame() {
        val newGameId = (1000..9999).random().toString()
        _gameId.value = newGameId

        val hostPlayer = Player(
            uid = auth.currentUser?.uid ?: "",
            name = auth.currentUser?.displayName ?: "Anónimo"
        )

        val initialGameRoom = GameRoom(
            roomId = newGameId,
            players = mapOf(hostPlayer.uid to hostPlayer)
        )

        firebaseService.createGameRoom(initialGameRoom)
        joinAndListenToGame(newGameId)
    }

    // --------------------------------------------------------------------
    // Join an existing game
    // --------------------------------------------------------------------

    fun joinGame(gameId: String) {
        val newPlayer = Player(
            uid = auth.currentUser?.uid ?: "",
            name = auth.currentUser?.displayName ?: "Anónimo"
        )

        viewModelScope.launch {
            joinAndListenToGame(gameId)

            // Wait for the current room
            val room = _gameRoom.filterNotNull().first()

            // Add the player using copy()
            val updatedRoom = room.copy(
                players = room.players + (newPlayer.uid to newPlayer)
            )

            firebaseService.updateGameRoom(updatedRoom)
        }
    }

    // --------------------------------------------------------------------
    // Listen for changes in Firebase
    // --------------------------------------------------------------------

    private fun joinAndListenToGame(gameId: String) {
        _gameId.value = gameId
        viewModelScope.launch {
            firebaseService.listenToGameRoom(gameId).collect {
                _gameRoom.value = it
            }
        }
    }

    // --------------------------------------------------------------------
    // Leave the game
    // --------------------------------------------------------------------

    fun leaveGame() {
        val room = _gameRoom.value ?: return
        val playerId = auth.currentUser?.uid ?: return

        val updatedRoom = room.copy(
            players = room.players - playerId
        )

        firebaseService.updateGameRoom(updatedRoom)
    }

    // --------------------------------------------------------------------
    // Start the game
    // --------------------------------------------------------------------

    fun startGame() {
        val room = _gameRoom.value ?: return

        val updatedRoom = GameLogic.startRound(room)
            .copy(isGameStarted = true)

        firebaseService.updateGameRoom(updatedRoom)
    }

    // --------------------------------------------------------------------
    // Submit a guess (emoji)
    // --------------------------------------------------------------------

    fun submitGuess(guess: String) {
        val room = _gameRoom.value ?: return
        val playerId = auth.currentUser?.uid ?: return

        val updatedRoom = GameLogic.checkGuess(room, playerId, guess)

        firebaseService.updateGameRoom(updatedRoom)
    }

    // --------------------------------------------------------------------
    // Chat within the game room
    // --------------------------------------------------------------------

    fun sendChatMessage(content: String) {
        val room = _gameRoom.value ?: return

        val message = Message(
            author = auth.currentUser?.displayName ?: "Anónimo",
            content = content,
            timestamp = System.currentTimeMillis()
        )

        val updatedRoom = room.copy(
            chat = room.chat + message
        )

        firebaseService.updateGameRoom(updatedRoom)
    }

    // --------------------------------------------------------------------
    // End the current round
    // --------------------------------------------------------------------

    fun endRound() {
        val room = _gameRoom.value ?: return

        val afterElimination = GameLogic.finishRound(room)

        // If there is already a winner:
        if (afterElimination.winnerId != null) {
            firebaseService.updateGameRoom(afterElimination)
            return
        }

        // If multiple players remain: start the next round
        val nextRound = GameLogic.startRound(afterElimination)

        firebaseService.updateGameRoom(nextRound)
    }
}
