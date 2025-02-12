package ca.mattlack.wordleapp.game

interface WordleEngine {
    suspend fun numDays(): Int
    suspend fun getWord(day: Int): String
    suspend fun guessWord(guess: String, word: String): WordleGuess
}