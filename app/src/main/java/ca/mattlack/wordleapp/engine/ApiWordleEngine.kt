package ca.mattlack.wordleapp.engine

import ca.mattlack.wordleapp.game.WordleEngine
import ca.mattlack.wordleapp.game.WordleGuess
import ca.mattlack.wordleapp.game.WordleLetterStatus
import retrofit2.http.GET

class ApiWordleEngine(private val api: WordleApi) : WordleEngine {
    override suspend fun numDays(): Int {
        return api.numDays().body()!!
    }

    override suspend fun getWord(day: Int): String {
        return api.getWord(day).body()!!
    }

    override suspend fun guessWord(guess: String, word: String): WordleGuess {
        return when (val res = api.guessWord(guess, word).body()!!) {
            is GuessResponse.Success -> WordleGuess(guess, res.result.map {
                when (it) {
                    '+' -> WordleLetterStatus.CORRECT
                    '-' -> WordleLetterStatus.INCORRECT
                    'x' -> WordleLetterStatus.CONTAINED
                    else -> throw IllegalStateException()
                }
            })
            is GuessResponse.Failure -> throw InvalidWordException()
        }
    }
}