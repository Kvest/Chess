package com.kvest.chess.ui.chess_board

import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.model.PieceType

data class PieceOnSquare(
    val pieceType: PieceType,
    val square: Square,
)
