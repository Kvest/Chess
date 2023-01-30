package com.kvest.chess.ui.chess_game

import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.ui.chess_board.PieceOnSquare

data class ChessGameUIState(
    val board: ChessBoard,
    val pieces: List<PieceOnSquare>,
    val selectedSquare: Square?,
    val squaresForMove: Set<Square>,
    val history: List<String>
)