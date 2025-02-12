package ca.mattlack.wordleapp.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class WordleGame(var word: String, private val engine: WordleEngine) {

    private val _guesses = MutableStateFlow(emptyList<WordleGuess>())
    val guesses: StateFlow<List<WordleGuess>> = _guesses

    private val _remainingTries = MutableStateFlow(6)
    val remainingTries: StateFlow<Int> = _remainingTries

    val gameResult get() = when {
        _guesses.value.lastOrNull()?.guess?.lowercase() == word.lowercase() -> GameResult.WON
        _remainingTries.value <= 0 -> GameResult.LOST
        else -> GameResult.PLAYING
    }

    fun reset() {
        _guesses.update { emptyList() }
        _remainingTries.update { 6 }
    }

    suspend fun guessWord(word: String) {
        if (gameResult != GameResult.PLAYING) return
        val result = engine.guessWord(word, this.word)
        _remainingTries.update { it - 1 }
        _guesses.update { it + result }
    }

    enum class GameResult {
        PLAYING,
        WON,
        LOST
    }
}