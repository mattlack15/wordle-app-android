package ca.mattlack.wordleapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.mattlack.wordleapp.viewmodel.DaysViewModel

@Composable
fun DaysScreen(modifier: Modifier = Modifier, days: List<DaysViewModel.WordleDay>, onSelectDay: (DaysViewModel.WordleDay) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(days) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(7.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(7.dp))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Day #${it.day}", fontWeight = FontWeight.Black)
                Button(onClick = { onSelectDay(it) }, shape = RoundedCornerShape(4.dp)) {
                    Text("Play")
                }
            }
        }
    }
}