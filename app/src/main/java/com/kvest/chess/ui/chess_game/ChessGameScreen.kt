package com.kvest.chess.ui.chess_game

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvest.chess.model.*
import com.kvest.chess.ui.chess_board.ChessBoard
import com.kvest.chess.ui.chess_board.ChessBoardListener
import com.kvest.chess.ui.game_history.GameHistory

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ChessGameScreen(
    chessGameViewModel: ChessGameViewModel = viewModel()
) {
    val game by chessGameViewModel.uiState.collectAsStateWithLifecycle()

    ChessGameScreen(game = game, listener = chessGameViewModel)
}

@Composable
fun ChessGameScreen(
    game: ChessGameUIState,
    listener: ChessBoardListener,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            val squareSize = ((LocalConfiguration.current.screenWidthDp - 16) / 8).dp

            ChessBoard(
                chessBoard = game.board,
                pieces = game.pieces,
                selectedSquare = game.selectedSquare,
                squaresForMove = game.squaresForMove,
                promotions = game.promotions,
                squareSize = squareSize,
                listener = listener,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            GameHistory(
                history = game.history,
                modifier = Modifier
                    .padding(top = 20.dp, start = 8.dp, end = 8.dp)
            )
        }
    } else {
        val squareSize = ((LocalConfiguration.current.screenHeightDp - 16) / 8).dp

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp)
        ) {
            ChessBoard(
                chessBoard = game.board,
                pieces = game.pieces,
                selectedSquare = game.selectedSquare,
                squaresForMove = game.squaresForMove,
                promotions = game.promotions,
                squareSize = squareSize,
                listener = listener,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            GameHistory(
                history = game.history,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp, end = 8.dp)
            )
        }
    }
}