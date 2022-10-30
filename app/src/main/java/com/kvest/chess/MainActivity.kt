package com.kvest.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvest.chess.ui.chess_board.ChessBoard
import com.kvest.chess.ui.chess_game.ChessGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    chessGameViewModel: ChessGameViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        val board by chessGameViewModel.game.collectAsState()
        ChessBoard(
            board = board,
            onCellClicked = chessGameViewModel::onCellClicked,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}