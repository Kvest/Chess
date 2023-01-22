package com.kvest.chess.ui.chess_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.model.*
import com.kvest.chess.ui.chess_board.ChessBoard
import com.kvest.chess.ui.game_history.GameHistory

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ChessGameScreen(
    chessGameViewModel: ChessGameViewModel = viewModel()
) {
    val game by chessGameViewModel.uiState.collectAsStateWithLifecycle()

    ChessGameScreen(game, chessGameViewModel::onCellClicked)
}

@Composable
fun ChessGameScreen(
    game: ChessGameUIState,
    onCellClicked: (Square) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ChessBoard(
            board = game.board,
            onCellClicked = onCellClicked,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        GameHistory(
            history = game.history,
            modifier = Modifier
                .padding(top = 20.dp)
        )
    }
}

@Preview(widthDp = 320, showBackground = true)
@Composable
fun chessGameViewModelPreview() {
    val rows = (0..7).map { rowId ->
        BoardRow(
            (0..7).map { columnId ->
                Cell(
                    Square.A1,
                    if ((rowId + columnId) % 2 == 0) CellType.LIGHT else CellType.DARK,
                    piece = if ((rowId + columnId) % 2 == 0) PieceType.PAWN_DARK else PieceType.PAWN_LIGHT,
                    isSelected = false,
                    isForMove = false,
                )
            }
        )
    }

    val board = BoardModel(rows)
    val history = listOf("1. e2-e4 e7-e5", "2. d4-d5")

    ChessGameScreen(ChessGameUIState(board, history)) { }
}