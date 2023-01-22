package com.kvest.chess.ui.utils

import com.github.bhlangonijr.chesslib.MoveBackup
import com.github.bhlangonijr.chesslib.Square

fun MoveBackup.isLongCastleMove(): Boolean {
    return this.isCastleMove && (this.move.to == Square.C1 || this.move.to == Square.C8)
}

fun MoveBackup.isShortCastleMove(): Boolean {
    return this.isCastleMove && (this.move.to == Square.G1 || this.move.to == Square.G8)
}