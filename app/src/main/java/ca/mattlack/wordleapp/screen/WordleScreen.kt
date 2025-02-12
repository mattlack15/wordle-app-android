package ca.mattlack.wordleapp.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.mattlack.wordleapp.game.WordleGuess
import ca.mattlack.wordleapp.game.WordleLetterStatus
import ca.mattlack.wordleapp.viewmodel.WordleViewModel
import ca.mattlack.wordleapp.viewmodel.WordleViewState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordleScreen(state: WordleViewState, onEvent: (WordleViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(bottom = 25.dp)) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .weight(0.75f)) {
            stickyHeader {
                Row(modifier = Modifier.fillMaxWidth().padding(start = 10.dp), horizontalArrangement = Arrangement.Start) {
                    IconButton(modifier = Modifier.background(MaterialTheme.colorScheme.surface), onClick = { onEvent(WordleViewModel.Event.Back) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            }
            items(state.guesses) { guess ->
                AnimatedVisibility(true, exit = ExitTransition.None, enter = slideInVertically()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        guess.guess.toList().zip(guess.statuses).forEach { (letter, status) ->
                            val color = when (status) {
                                WordleLetterStatus.CORRECT -> Color(0xFF77FF77)
                                WordleLetterStatus.CONTAINED -> Color(0xFFEEEE77)
                                WordleLetterStatus.INCORRECT -> Color(0xFF999999)
                            }
                            Box(modifier = Modifier.fillMaxSize()
                                .weight(1f)
                                .aspectRatio(1.0f)
                                .background(color)
                                .border(1.dp, Color.Black),
                                contentAlignment = Alignment.Center) {
                                Text(
                                    text = letter.toString().uppercase(),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        TextField(
            modifier = Modifier.fillMaxWidth(0.75f).align(Alignment.CenterHorizontally),
            value = state.guessField,
            onValueChange = { onEvent(WordleViewModel.Event.SetGuess(it.trim().uppercase())) },
            label = { Text("Guess") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onEvent(WordleViewModel.Event.Guess)
            }),
        )
    }
}

@Preview
@Composable
private fun WordleScreenPreview() {
    WordleScreen(
        WordleViewState(
            remainingTries = 5,
            guesses = listOf(
                WordleGuess("hello", listOf(WordleLetterStatus.CORRECT, WordleLetterStatus.CONTAINED, WordleLetterStatus.INCORRECT, WordleLetterStatus.CORRECT, WordleLetterStatus.CORRECT)),
                WordleGuess("grape", listOf(WordleLetterStatus.CORRECT, WordleLetterStatus.CONTAINED, WordleLetterStatus.INCORRECT, WordleLetterStatus.CORRECT, WordleLetterStatus.CORRECT))
        ),
            guessField = ""
        ),
        onEvent = {}
    )
}