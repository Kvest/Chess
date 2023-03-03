package com.kvest.chess.ui.chess_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.MoveBackup
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.kvest.chess.ext.toPieceType
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.model.PieceType
import com.kvest.chess.ui.chess_board.PieceOnSquare
import com.kvest.chess.ui.utils.isLongCastleMove
import com.kvest.chess.ui.utils.isShortCastleMove
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessGameViewModel : ViewModel() {
    private val chessBoard = ChessBoard()
    private val board by lazy { Board() }
    private var selectedCell: Square? = null
    private var pieceId = 0

    private val _uiState = MutableStateFlow(
        ChessGameUIState(
            board = chessBoard,
            pieces = emptyList<PieceOnSquare>().toImmutableList(),
            selectedSquare = selectedCell,
            squaresForMove = emptySet<Square>().toImmutableSet(),
            history = emptyList<String>().toImmutableList(),
        )
    )
    val uiState: StateFlow<ChessGameUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Init board on Default dispatcher
            withContext(Dispatchers.Default) {
                board.toString()
            }

            emitCurrentUI()
        }
    }

    fun onSquareClicked(square: Square) {
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

    fun onTakePiece(square: Square) {
        if (board.getPiece(square).pieceSide == board.sideToMove) {
            selectedCell = square

            emitCurrentUI()
        }
    }

    fun onReleasePiece(square: Square) {
        val move = Move(selectedCell, square)
        val canDoMove = move in board.legalMoves()

        if (canDoMove) {
            board.doMove(move)
        }

        selectedCell = null

        emitCurrentUI()
    }

    private fun emitCurrentUI() {
        _uiState.update { oldUiState ->
            val pieces = calculatePiecesOnSquares(oldUiState.pieces)

            val squaresForMove = board
                .legalMoves()
                .filter { it.from == selectedCell }
                .map { it.to }
                .toImmutableSet()

            val currentHistory = board
                .backup
                .chunked(2)
                .mapIndexed { index, moves ->
                    "${index + 1}. ${moves[0].toHistoryString()} ${moves.getOrNull(1).toHistoryString()}"
                }

            ChessGameUIState(
                board = chessBoard,
                pieces = pieces.toImmutableList(),
                selectedSquare = selectedCell,
                squaresForMove = squaresForMove,
                history = currentHistory.toImmutableList()
            )
        }
    }

    private fun calculatePiecesOnSquares(pieces: List<PieceOnSquare>): List<PieceOnSquare> {
        // initial calculation
        if (pieces.isEmpty()) {
            return Square.values()
                .mapNotNull { square ->
                    board.getPiece(square)
                        .toPieceType()
                        ?.let { pieceType -> PieceOnSquare(pieceId++, pieceType, square) }
                }
        }

        val oldPiecesMap = pieces.associateBy { it.square }.toMutableMap()
        val notAddedPieces = mutableMapOf<Square, PieceType>()

        return buildList {
            // Add all not moved pieces to the list and create the map of pieces that were not found on the old squares
            Square.values()
                .forEach { square ->
                    val pieceType = board.getPiece(square).toPieceType()

                    if (pieceType != null) {
                        val oldPiece = oldPiecesMap[square]
                        if (oldPiece?.pieceType == pieceType) {
                            add(oldPiece)

                            oldPiecesMap.remove(square)
                        } else {
                            notAddedPieces[square] = pieceType
                        }
                    }
                }

            notAddedPieces.forEach { (square, pieceType) ->
                val id = oldPiecesMap.values.find { it.pieceType == pieceType }?.id
                requireNotNull(id) // TODO - will crash in case of promotion

                add(PieceOnSquare(id, pieceType, square))
            }
        }
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