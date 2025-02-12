package ca.mattlack.wordleapp.game

data class WordleGuess(
    val guess: String,
    val statuses: List<WordleLetterStatus>
)