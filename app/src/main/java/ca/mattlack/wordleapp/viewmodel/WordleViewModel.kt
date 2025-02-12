package ca.mattlack.wordleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.mattlack.wordleapp.engine.InvalidWordException
import ca.mattlack.wordleapp.game.WordleEngine
import ca.mattlack.wordleapp.game.WordleGame
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WordleViewModel(val game: WordleGame) : ViewModel() {

    private val _currentGuess = MutableStateFlow("")

    private val _uiEvents = MutableSharedFlow<UIEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    val state = combine(
        _currentGuess,
        game.guesses,
        game.remainingTries
    ) { guess, guesses, remainingTries ->
        WordleViewState(
            remainingTries = remainingTries,
            guesses = guesses,
            guessField = guess
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WordleViewState())

    fun onEvent(event: Event) {
        when (event) {
            Event.Guess -> guessWord(_currentGuess.value)
            is Event.SetGuess -> _currentGuess.value = event.guess
            Event.Back -> viewModelScope.launch {
                _uiEvents.emit(UIEvent.NavigateBack)
            }
        }
    }

    private fun guessWord(word: String) {
        if (state.value.guessField.isEmpty()) return
        if (game.gameResult != WordleGame.GameResult.PLAYING) {
            viewModelScope.launch {
                _uiEvents.emit(UIEvent.Snackbar("Game is over!"))
            }
        }
        _currentGuess.update { "" }
        viewModelScope.launch {
            try {
                game.guessWord(word)
            } catch (e: InvalidWordException) {
                _uiEvents.emit(UIEvent.Snackbar("That is not a valid word!"))
            }
            _currentGuess.value = ""
        }
    }

    sealed interface Event {
        class SetGuess(val guess: String) : Event
        data object Guess : Event
        data object Back : Event
    }

    sealed interface UIEvent {
        data class Snackbar(val message: String) : UIEvent
        data object NavigateBack : UIEvent
    }

}