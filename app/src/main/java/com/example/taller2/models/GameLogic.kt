package com.example.taller2.models

object GameLogic {


    val EMOJIS = listOf(
        "😂","🔥","🍕","🎉","😎","🐱","🌟","❤","🚀","🤖",
        "😈","🍔","⚽","🎮","👻","🙈","🐶","🍩","💀","🧠"
    )


    fun assignEmojis(players: Map<String, Player>): Map<String, String> {
        return players.keys.associateWith { EMOJIS.random() }
    }


    fun startRound(room: GameRoom, roundDurationSec: Int = 30): GameRoom {
        val now = System.currentTimeMillis()


        if (room.players.size <= 1) {
            return room.copy(
                winnerId = room.players.keys.firstOrNull()
            )
        }

        return room.copy(
            roundNumber = room.roundNumber + 1,
            correctPlayers = emptyList(),
            eliminatedPlayers = emptyList(),
            roundEndTime = now + (roundDurationSec * 1000),
            playerEmojis = assignEmojis(room.players)
        )
    }


    fun checkGuess(room: GameRoom, playerId: String, guessed: String): GameRoom {
        val realEmoji = room.playerEmojis[playerId] ?: return room

        return if (guessed == realEmoji) {
            if (!room.correctPlayers.contains(playerId)) {
                room.copy(correctPlayers = room.correctPlayers + playerId)
            } else room
        } else {
            room
        }
    }


    fun finishRound(room: GameRoom): GameRoom {
        val survivors = room.correctPlayers.toSet()
        val allPlayers = room.players.keys

        val eliminated = allPlayers.filter { it !in survivors }


        val remainingPlayers = room.players.filterKeys { it in survivors }


        if (remainingPlayers.size == 1) {
            return room.copy(
                players = remainingPlayers,
                eliminatedPlayers = eliminated,
                winnerId = remainingPlayers.keys.first()
            )
        }

        return room.copy(
            players = remainingPlayers,
            eliminatedPlayers = eliminated,
            playerEmojis = room.playerEmojis.filterKeys { it in survivors }
        )
    }
}