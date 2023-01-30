package com.kvest.chess.ui.chess_game

import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.ui.chess_board.PieceOnSquare
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

data class ChessGameUIState(
    val board: ChessBoard,
    val pieces: ImmutableList<PieceOnSquare>,
    val selectedSquare: Square?,
    val squaresForMove: ImmutableSet<Square>,
    val history: ImmutableList<String>
)