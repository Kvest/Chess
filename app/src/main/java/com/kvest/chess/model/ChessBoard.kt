package com.kvest.chess.model

import com.github.bhlangonijr.chesslib.Square

private const val BOARD_SIZE = 8

class ChessBoard {
    val size: Int
        get() = BOARD_SIZE

    operator fun get(row: Int, column: Int): Square {
        if ((row !in 0 until BOARD_SIZE) || (column !in 0 until BOARD_SIZE)) {
            return Square.NONE
        }

        return Square.values()[(size * size) - (row + 1) * size + column]
    }

    fun getRow(square: Square): Int {
        val index = Square.values().indexOfFirst { it == square }
        return size - (index / size) - 1
    }

    fun getColumn(square: Square): Int {
        val index = Square.values().indexOfFirst { it == square }

        return index % size
    }
}