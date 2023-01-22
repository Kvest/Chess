package com.kvest.chess.ui.chess_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.MoveBackup
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.kvest.chess.model.*
import com.kvest.chess.ui.utils.isLongCastleMove
import com.kvest.chess.ui.utils.isShortCastleMove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val BOARD_SIZE = 8

class ChessGameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChessGameUIState(
            board = generateEmptyBoard(),
            history = emptyList()
        )
    )
    val uiState: StateFlow<ChessGameUIState> = _uiState.asStateFlow()

    private val board by lazy { Board() }
    private var selectedCell: Square? = null

    init {
        viewModelScope.launch {
            //Init board on Default dispatcher
            withContext(Dispatchers.Default) {
                board.toString()
            }

            emitCurrentBoard()
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

        emitCurrentBoard()
    }

    private fun emitCurrentBoard() {
        val currentBoard = generateBoard(board = board) { square ->
            board.getPiece(square).toPieceType()
        }

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
                board = currentBoard,
                history = currentHistory
            )
        )
    }

    private fun generateEmptyBoard(): BoardModel = generateBoard(board = null) { _ -> null }

    private fun generateBoard(board: Board?, calculatePiece: (Square) -> PieceType?): BoardModel {
        val squaresForMove = board
            ?.legalMoves()
            ?.filter { it.from == selectedCell }
            ?.map { it.to }
            ?.toSet()
            .orEmpty()

        val squares = Square.values()

        val rows = (BOARD_SIZE - 1 downTo 0).map { rowId ->
            BoardRow(
                (0 until BOARD_SIZE).map { columnId ->
                    val square = squares[rowId * BOARD_SIZE + columnId]
                    val isForMove = square in squaresForMove

                    Cell(
                        square = square,
                        type = if (square.isLightSquare) CellType.LIGHT else CellType.DARK,
                        piece = calculatePiece(square),
                        isSelected = (square == selectedCell),
                        isForMove = isForMove,
                    )
                }
            )
        }

        return BoardModel(rows)
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

private fun Piece.toPieceType(): PieceType? {
    return when (this) {
        Piece.WHITE_PAWN -> PieceType.PAWN_LIGHT
        Piece.WHITE_KNIGHT -> PieceType.KNIGHT_LIGHT
        Piece.WHITE_BISHOP -> PieceType.BISHOP_LIGHT
        Piece.WHITE_ROOK -> PieceType.ROOK_LIGHT
        Piece.WHITE_QUEEN -> PieceType.QUEEN_LIGHT
        Piece.WHITE_KING -> PieceType.KING_LIGHT
        Piece.BLACK_PAWN -> PieceType.PAWN_DARK
        Piece.BLACK_KNIGHT -> PieceType.KNIGHT_DARK
        Piece.BLACK_BISHOP -> PieceType.BISHOP_DARK
        Piece.BLACK_ROOK -> PieceType.ROOK_DARK
        Piece.BLACK_QUEEN -> PieceType.QUEEN_DARK
        Piece.BLACK_KING -> PieceType.KING_DARK
        Piece.NONE -> null
    }
}