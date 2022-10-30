package com.kvest.chess.model

import com.github.bhlangonijr.chesslib.Square

data class BoardModel(
    val rows: List<BoardRow>
)

data class BoardRow(
    val cells: List<Cell>
)

data class Cell(
    val square: Square,
    val type: CellType,
    val piece: PieceType?,
    val isSelected: Boolean,
    val isForMove: Boolean,
)