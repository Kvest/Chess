package com.kvest.chess.ext

import com.github.bhlangonijr.chesslib.Piece
import com.kvest.chess.model.PieceType

fun PieceType.toPiece(): Piece {
    return when (this) {
        PieceType.PAWN_LIGHT -> Piece.WHITE_PAWN
        PieceType.KNIGHT_LIGHT -> Piece.WHITE_KNIGHT
        PieceType.BISHOP_LIGHT -> Piece.WHITE_BISHOP
        PieceType.ROOK_LIGHT -> Piece.WHITE_ROOK
        PieceType.QUEEN_LIGHT -> Piece.WHITE_QUEEN
        PieceType.KING_LIGHT -> Piece.WHITE_KING
        PieceType.PAWN_DARK -> Piece.BLACK_PAWN
        PieceType.KNIGHT_DARK -> Piece.BLACK_KNIGHT
        PieceType.BISHOP_DARK -> Piece.BLACK_BISHOP
        PieceType.ROOK_DARK -> Piece.BLACK_ROOK
        PieceType.QUEEN_DARK -> Piece.BLACK_QUEEN
        PieceType.KING_DARK -> Piece.BLACK_KING
    }
}