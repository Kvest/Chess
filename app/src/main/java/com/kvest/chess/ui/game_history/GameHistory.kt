package com.kvest.chess.ui.game_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvest.chess.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GameHistory(
    history: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.moves_title),
                fontSize = 24.sp,
            )
        }
        items(history) { item ->
            GameHistoryItem(item)
        }
    }

    //Scroll to the end of the history when it changes
    LaunchedEffect(history) {
        if (history.isNotEmpty()) {
            listState.scrollToItem(history.lastIndex)
        }
    }
}

@Composable
fun GameHistoryItem(
    historyItem: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = historyItem,
        modifier = modifier,
        fontSize = 24.sp
    )
}

@Preview(widthDp = 320, showBackground = true)
@Composable
fun GameHistoryPreview() {
    GameHistory(
        persistentListOf(
            "1. ♙e2-e4 ♟e7-e5",
            "2. ♘g1-f3 ♞g8-f6",
            "3. ♗f1-c4"
        ),
        modifier = Modifier
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
    )
}