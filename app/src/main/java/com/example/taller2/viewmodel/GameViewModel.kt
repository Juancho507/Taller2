package com.example.taller2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.taller2.data.FirebaseService
import com.example.taller2.models.GameRoom
import com.example.taller2.models.Message
import com.example.taller2.models.Player
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// --- CONSTRUCTOR MODIFICADO ---
class GameViewModel(
    private val firebaseService: FirebaseService,
    private val auth: FirebaseAuth
) : ViewModel() {

    // Ya no creamos las instancias aquí

    private val _gameRoom = MutableStateFlow<GameRoom?>(null)
    val gameRoom: StateFlow<GameRoom?> = _gameRoom.asStateFlow()

    private val _gameId = MutableStateFlow<String?>(null)
    val gameId: StateFlow<String?> = _gameId.asStateFlow()

    private val _joinRoomError = MutableStateFlow<String?>(null)
    val joinRoomError: StateFlow<String?> = _joinRoomError.asStateFlow()

    fun createGame() {
        _joinRoomError.value = null
        val newGameId = (1000..9999).random().toString()
        _gameId.value = newGameId

        val hostPlayer = Player().apply {
            uid = auth.currentUser?.uid ?: ""
            name = auth.currentUser?.displayName ?: "Anónimo"
        }
        val initialGameRoom = GameRoom().apply {
            roomId = newGameId
            players[hostPlayer.uid] = hostPlayer
        }

        firebaseService.createGameRoom(initialGameRoom)
        listenToGame(newGameId)
    }

    fun joinGame(gameId: String) {
        _joinRoomError.value = null
        viewModelScope.launch {
            val player = Player().apply {
                uid = auth.currentUser?.uid ?: ""
                name = auth.currentUser?.displayName ?: "Anónimo"
            }
            val success = firebaseService.joinGameAtomically(gameId, player)
            if (success) {
                listenToGame(gameId)
            } else {
                _joinRoomError.value = "No se pudo unir a la sala. Puede que no exista o que la partida ya haya comenzado."
            }
        }
    }

    private fun listenToGame(gameId: String) {
        _gameId.value = gameId
        viewModelScope.launch {
            firebaseService.listenToGameRoom(gameId).collect { _gameRoom.value = it }
        }
    }

    fun leaveGame() {
        viewModelScope.launch {
            val gameId = _gameId.value ?: return@launch
            val playerId = auth.currentUser?.uid ?: return@launch
            firebaseService.leaveGameAtomically(gameId, playerId)
        }
    }

    fun startGame() {
        viewModelScope.launch {
            val gameId = _gameId.value ?: return@launch
            firebaseService.startGameAtomically(gameId)
        }
    }

    fun submitGuess(guessedEmoji: String) {
        val room = _gameRoom.value ?: return
        val playerId = auth.currentUser?.uid ?: return
        val player = room.players[playerId] ?: return

        if (player.emoji != guessedEmoji) {
            eliminatePlayer(room, playerId)
        } else {
            moveToNextPlayer(room)
        }
    }

    fun handleTimeout() {
        val room = _gameRoom.value ?: return
        if (room.currentPlayerId == auth.currentUser?.uid && System.currentTimeMillis() >= room.roundEndTime) {
            room.currentPlayerId?.let { timedOutPlayerId ->
                eliminatePlayer(room, timedOutPlayerId)
            }
        }
    }

    private fun moveToNextPlayer(currentRoom: GameRoom) {
        val activePlayers = currentRoom.players.values.filter { !it.isEliminated }.sortedBy { it.name }
        if (activePlayers.isEmpty()) return

        val currentIndex = activePlayers.indexOfFirst { it.uid == currentRoom.currentPlayerId }
        val nextIndex = (currentIndex + 1) % activePlayers.size
        currentRoom.currentPlayerId = activePlayers.getOrNull(nextIndex)?.uid
        currentRoom.roundEndTime = System.currentTimeMillis() + 30000

        firebaseService.updateGameRoom(currentRoom)
    }

    private fun eliminatePlayer(currentRoom: GameRoom, playerId: String) {
        currentRoom.players[playerId]?.isEliminated = true

        val activePlayers = currentRoom.players.values.filter { !it.isEliminated }
        if (activePlayers.size <= 1) {
            currentRoom.winnerId = activePlayers.firstOrNull()?.uid
            currentRoom.isGameStarted = false
        } else if (currentRoom.currentPlayerId == playerId) {
            moveToNextPlayer(currentRoom)
        }

        firebaseService.updateGameRoom(currentRoom)
    }

    fun sendChatMessage(content: String) {
        val room = _gameRoom.value ?: return
        val message = Message().apply {
            author = auth.currentUser?.displayName ?: "Anónimo"
            this.content = content
            timestamp = System.currentTimeMillis()
        }
        room.chat = room.chat + message
        firebaseService.updateGameRoom(room)
    }
}

// --- FÁBRICA DE VIEWMODEL ---
// Le dice a la UI cómo crear un GameViewModel con sus dependencias.
class GameViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(FirebaseService(), FirebaseAuth.getInstance()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
