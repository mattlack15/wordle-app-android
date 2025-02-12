package ca.mattlack.wordleapp.engine

import ca.mattlack.wordleapp.game.WordleEngine
import ca.mattlack.wordleapp.game.WordleGuess
import ca.mattlack.wordleapp.game.WordleLetterStatus

class DefaultWordleEngine : WordleEngine {

    private val words = listOf(
        "hello",
        "world",
        "apple",
        "false",
        "brave",
        "grape",
        "chair",
        "track"
    )

    override suspend fun numDays(): Int {
        return words.size
    }

    override suspend fun getWord(day: Int): String {
        return words[day]
    }

    override suspend fun guessWord(guess: String, word: String): WordleGuess {
        if (guess.length != word.length) throw InvalidWordException()
        return guess.lowercase().zip(word.lowercase()).map { (g, t) ->
            when (g) {
                t -> WordleLetterStatus.CORRECT
                in word -> WordleLetterStatus.CONTAINED
                else -> WordleLetterStatus.INCORRECT
            }
        }.let { WordleGuess(guess, it) }
    }
}