package com.kvest.chess.model

import com.github.bhlangonijr.chesslib.Square

private const val BOARD_SIZE = 8

class ChessBoard {
    val size: Int
        get() = BOARD_SIZE

    operator fun get(row: Int, column: Int): Square {
        require(row >= 0 && row < BOARD_SIZE)
        require(column >= 0 && column < BOARD_SIZE)

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