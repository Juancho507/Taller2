package com.example.taller2.models

object GameLogic {

    // List of all possible emojis that can be assigned to players each round
    val EMOJIS = listOf(
        "😂","🔥","🍕","🎉","😎","🐱","🌟","❤️","🚀","🤖",
        "😈","🍔","⚽","🎮","👻","🙈","🐶","🍩","💀","🧠"
    )

    /**
     * Assigns a random emoji to each player.
     * @param players A map of playerId -> Player object
     * @return A map of playerId -> assigned emoji
     */
    fun assignEmojis(players: Map<String, Player>): Map<String, String> {
        return players.keys.associateWith { EMOJIS.random() }
    }

    /**
     * Starts a new round.
     * Resets round status, assigns new emojis, and sets the round timer.
     * @param room Current GameRoom state
     * @param roundDurationSec Length of the round in seconds (default: 30)
     * @return Updated GameRoom with the new round initialized
     */
    fun startRound(room: GameRoom, roundDurationSec: Int = 30): GameRoom {
        val now = System.currentTimeMillis()

        // If only one player remains, they automatically win
        if (room.players.size <= 1) {
            return room.copy(
                winnerId = room.players.keys.firstOrNull()
            )
        }

        return room.copy(
            roundNumber = room.roundNumber + 1,          // Increment round count
            correctPlayers = emptyList(),                 // Reset correct players
            eliminatedPlayers = emptyList(),              // Reset eliminations
            roundEndTime = now + (roundDurationSec * 1000), // Round end timestamp
            playerEmojis = assignEmojis(room.players)     // Assign new emojis
        )
    }

    /**
     * Checks whether the player guessed their emoji correctly.
     * If correct, adds them to the list of surviving (correct) players.
     * @param room The current GameRoom state
     * @param playerId The ID of the player making the guess
     * @param guessed The emoji guessed by the player
     * @return Updated GameRoom after processing the guess
     */
    fun checkGuess(room: GameRoom, playerId: String, guessed: String): GameRoom {
        val realEmoji = room.playerEmojis[playerId] ?: return room

        return if (guessed == realEmoji) {
            // Add player to correctPlayers only if they're not already listed
            if (!room.correctPlayers.contains(playerId)) {
                room.copy(correctPlayers = room.correctPlayers + playerId)
            } else room
        } else {
            // Incorrect guesses do not change the game state
            room
        }
    }

    /**
     * Finalizes the round:
     * - Eliminates players who did not guess correctly
     * - Keeps only players who guessed right
     * - Determines if a winner exists
     * @param room The current GameRoom state
     * @return Updated GameRoom after finishing the round
     */
    fun finishRound(room: GameRoom): GameRoom {
        val survivors = room.correctPlayers.toSet()       // Players who guessed correctly
        val allPlayers = room.players.keys               // All players

        // Players who did NOT survive the round
        val eliminated = allPlayers.filter { it !in survivors }

        // Remaining players after eliminations
        val remainingPlayers = room.players.filterKeys { it in survivors }

        // If only one player remains → they win the game
        if (remainingPlayers.size == 1) {
            return room.copy(
                players = remainingPlayers,
                eliminatedPlayers = eliminated,
                winnerId = remainingPlayers.keys.first()
            )
        }

        // Otherwise, return updated game state and prepare for the next round
        return room.copy(
            players = remainingPlayers,
            eliminatedPlayers = eliminated,
            playerEmojis = room.playerEmojis.filterKeys { it in survivors } // Remove unused emojis
        )
    }
}
