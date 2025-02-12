package ca.mattlack.wordleapp.viewmodel

import ca.mattlack.wordleapp.game.WordleGuess

data class WordleViewState(
    val remainingTries: Int = 6,
    val guesses: List<WordleGuess> = emptyList(),
    val guessField: String = ""
)
