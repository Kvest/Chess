package com.kvest.chess.ui.chess_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.R
import com.kvest.chess.model.*
import com.kvest.chess.ui.chess_board.ChessBoard
import com.kvest.chess.ui.game_history.GameHistory

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ChessGameScreen(
    chessGameViewModel: ChessGameViewModel = viewModel()
) {
    val game by chessGameViewModel.uiState.collectAsStateWithLifecycle()

    ChessGameScreen(
        game = game,
        onSquareClicked = chessGameViewModel::onSquareClicked,
        onTakePiece = chessGameViewModel::onTakePiece,
        onReleasePiece = chessGameViewModel::onReleasePiece,
        onPromotionPieceTypeSelected = chessGameViewModel::onPromotionPieceTypeSelected,
    )
}

@Composable
fun ChessGameScreen(
    game: ChessGameUIState,
    onSquareClicked: (Square) -> Unit,
    onTakePiece: (Square) -> Unit,
    onReleasePiece: (Square) -> Unit,
    onPromotionPieceTypeSelected: (PieceType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        ChessBoard(
            chessBoard = game.board,
            pieces = game.pieces,
            selectedSquare = game.selectedSquare,
            squaresForMove = game.squaresForMove,
            promotions = game.promotions,
            squareSize = dimensionResource(R.dimen.square_size),
            onSquareClicked = onSquareClicked,
            onTakePiece = onTakePiece,
            onReleasePiece = onReleasePiece,
            onPromotionPieceTypeSelected = onPromotionPieceTypeSelected,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        GameHistory(
            history = game.history,
            modifier = Modifier
                .padding(top = 20.dp)
        )
    }
}