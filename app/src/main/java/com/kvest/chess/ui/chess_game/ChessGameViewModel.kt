package com.kvest.chess.ui.chess_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.MoveBackup
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.kvest.chess.ext.toPieceType
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.ui.chess_board.PieceOnSquare
import com.kvest.chess.ui.utils.isLongCastleMove
import com.kvest.chess.ui.utils.isShortCastleMove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessGameViewModel : ViewModel() {
    private val chessBoard = ChessBoard()
    private val board by lazy { Board() }
    private var selectedCell: Square? = null

    private val _uiState = MutableStateFlow(
        ChessGameUIState(
            board = chessBoard,
            pieces = emptyList(),
            selectedSquare = selectedCell,
            squaresForMove = emptyList(),
            history = emptyList(),
        )
    )
    val uiState: StateFlow<ChessGameUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            //Init board on Default dispatcher
            withContext(Dispatchers.Default) {
                board.toString()
            }

            emitCurrentUI()
        }
    }

    fun onCellClicked(square: Square) {
        if (board.getPiece(square).pieceSide == board.sideToMove) {
            selectedCell = square
        } else {
            val move = Move(selectedCell, square)
            val canDoMove = move in board.legalMoves()

            if (canDoMove) {
                board.doMove(move)
            }

            selectedCell = null
        }

        emitCurrentUI()
    }

    private fun emitCurrentUI() {
        val pieces = Square.values()
            .mapNotNull { square ->
                board.getPiece(square)
                    .toPieceType()
                    ?.let { pieceType -> PieceOnSquare(pieceType, square) }
            }

        val squaresForMove = board
            .legalMoves()
            .filter { it.from == selectedCell }
            .map { it.to }

        val currentHistory = board
            .backup
            .chunked(2)
            .mapIndexed { index, moves ->
                "${index + 1}. ${moves[0].toHistoryString()} ${
                    moves.getOrNull(1).toHistoryString()
                }"
            }

        _uiState.tryEmit(
            ChessGameUIState(
                board = chessBoard,
                pieces = pieces,
                selectedSquare = selectedCell,
                squaresForMove = squaresForMove,
                history = currentHistory
            )
        )
    }
}

private fun MoveBackup?.toHistoryString(): String {
    if (this == null) {
        return ""
    }

    return when {
        this.isLongCastleMove() -> "0-0-0"
        this.isShortCastleMove() -> "0-0"
        else -> "${this.movingPiece.fanSymbol} ${this.move.from}-${this.move.to}"
    }
}