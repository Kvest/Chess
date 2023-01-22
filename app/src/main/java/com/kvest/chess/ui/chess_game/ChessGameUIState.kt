package com.kvest.chess.ui.chess_game

import com.kvest.chess.model.BoardModel

data class ChessGameUIState(
    val board: BoardModel,
    val history: List<String>
)