package com.kvest.chess.ui.chess_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.kvest.chess.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val BOARD_SIZE = 8

class ChessGameViewModel : ViewModel() {
    private val _game = MutableStateFlow(generateEmptyBoard())
    val game: StateFlow<BoardModel> = _game.asStateFlow()

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

        _game.tryEmit(currentBoard)
    }

    private fun generateEmptyBoard(): BoardModel = generateBoard(board = null) { _ -> null }

    private fun generateBoard(board: Board?, calculatePiece: (Square) -> PieceType?): BoardModel {
        val legalMove = board?.legalMoves().orEmpty()

        val rows = (0 until BOARD_SIZE).map { rowId ->
            BoardRow(
                (0 until BOARD_SIZE).map { columnId ->
                    val square = Square.values()[rowId * BOARD_SIZE + columnId]
                    val isForMove = legalMove.contains(Move(selectedCell, square))

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