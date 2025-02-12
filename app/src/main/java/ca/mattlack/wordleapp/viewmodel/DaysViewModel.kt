package ca.mattlack.wordleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.mattlack.wordleapp.game.WordleEngine
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class DaysViewModel(private val engine: WordleEngine) : ViewModel() {

    data class WordleDay(val day: Int, val word: String)

    private val _days = MutableStateFlow<List<WordleDay>>(emptyList())
    val days: StateFlow<List<WordleDay>> = _days

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
        .onStart { loadDays() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000),
            false
        )

    private fun loadDays() {
        viewModelScope.launch {
            val numDays = engine.numDays()
            val words = mutableListOf<WordleDay>()
            _loading.update { true }
            supervisorScope {
                (0..<numDays).map { async { WordleDay(it, engine.getWord(it)) } }
                    .forEach { words.add(it.await()) }
            }
            _days.update { words }
            _loading.update { false }
        }
    }
}