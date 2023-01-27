package com.kvest.chess.ui.chess_board

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.R
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.model.PieceType
import com.kvest.chess.ui.theme.Copper
import kotlin.math.roundToInt

@Composable
fun ChessBoard(
    chessBoard: ChessBoard,
    pieces: List<PieceOnSquare>,
    selectedSquare: Square?,
    squaresForMove: List<Square>,
    squareSize: Dp,
    onCellClicked: (Square) -> Unit,
    modifier: Modifier = Modifier
) {
    val squareSizePx = with(LocalDensity.current) {
        squareSize.toPx()
    }

    Box(
        modifier = modifier
            .size(squareSize * 8)
            .drawWithCache {
                val size = Size(squareSizePx, squareSizePx)

                onDrawBehind {
                    drawSquares(chessBoard, squareSizePx, size)
                    drawSelectedSquare(chessBoard, selectedSquare, squareSizePx, size)
                    drawSquaresForPossibleMoves(chessBoard, squaresForMove, squareSizePx)
                }
            }
            .border(2.dp, color = Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val row = (offset.y / squareSizePx).toInt()
                    val column = (offset.x / squareSizePx).toInt()
                    val square = chessBoard[row, column]
                    onCellClicked(square)
                }
            }
    ) {
        pieces.forEach {
            val row = chessBoard.getRow(it.square)
            val column = chessBoard.getColumn(it.square)

            Piece(
                pieceType = it.pieceType,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (column * squareSizePx).roundToInt(),
                            y = (row * squareSizePx).roundToInt(),
                        )
                    }
                    .size(squareSize)
            )
        }
    }
}

private fun DrawScope.drawSquares(chessBoard: ChessBoard, squareSizePx: Float, size: Size) {
    for (row in 0 until chessBoard.size) {
        for (column in 0 until chessBoard.size) {
            drawRect(
                color = if (chessBoard[row, column].isLightSquare) Color.White else Copper,
                topLeft = Offset(x = column * squareSizePx, y = row * squareSizePx),
                size = size
            )
        }
    }
}

private fun DrawScope.drawSelectedSquare(
    chessBoard: ChessBoard,
    selectedSquare: Square?,
    squareSizePx: Float,
    size: Size,
) {
    if (selectedSquare != null) {
        val row = chessBoard.getRow(selectedSquare)
        val column = chessBoard.getColumn(selectedSquare)

        drawRect(
            color = Color.Yellow.copy(alpha = 0.6f),
            topLeft = Offset(x = column * squareSizePx, y = row * squareSizePx),
            size = size
        )
    }
}

private fun DrawScope.drawSquaresForPossibleMoves(
    chessBoard: ChessBoard,
    squaresForMove: List<Square>,
    squareSizePx: Float,
) {
    squaresForMove.forEach { square ->
        val row = chessBoard.getRow(square)
        val column = chessBoard.getColumn(square)

        drawCircle(
            color = Color.Black.copy(alpha = 0.3f),
            center = Offset(
                x = column * squareSizePx + squareSizePx / 2,
                y = row * squareSizePx + squareSizePx / 2
            ),
            radius = (squareSizePx) / 3
        )
    }
}

@Composable
fun Piece(pieceType: PieceType, modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        contentScale = ContentScale.Fit,
        painter = painterResource(getPieceTypeImageRes(pieceType)),
        contentDescription = stringResource(getPieceTypeDescriptionRes(pieceType))
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
fun ChessBoardPreview() {
    ChessBoard(
        chessBoard = ChessBoard(),
        pieces = listOf(
            PieceOnSquare(PieceType.PAWN_LIGHT, Square.A2),
            PieceOnSquare(PieceType.PAWN_DARK, Square.A7),
            PieceOnSquare(PieceType.ROOK_DARK, Square.A8),
            PieceOnSquare(PieceType.ROOK_LIGHT, Square.A1),
            PieceOnSquare(PieceType.KNIGHT_LIGHT, Square.G1),
        ),
        selectedSquare = Square.C2,
        squaresForMove = listOf(Square.A1, Square.A3, Square.B4, Square.D4, Square.E2),
        squareSize = 48.dp,
        onCellClicked = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
fun PiecePreview() {
    Row {
        Piece(
            pieceType = PieceType.BISHOP_DARK,
            modifier = Modifier.size(48.dp)
        )
        Piece(
            pieceType = PieceType.PAWN_LIGHT,
            modifier = Modifier.size(48.dp)
        )
    }
}

private fun getPieceTypeImageRes(pieceType: PieceType): Int =
    when (pieceType) {
        PieceType.KING_LIGHT -> R.drawable.king_light
        PieceType.QUEEN_LIGHT -> R.drawable.queen_light
        PieceType.ROOK_LIGHT -> R.drawable.rook_light
        PieceType.BISHOP_LIGHT -> R.drawable.bishop_light
        PieceType.KNIGHT_LIGHT -> R.drawable.knight_light
        PieceType.PAWN_LIGHT -> R.drawable.pawn_light
        PieceType.KING_DARK -> R.drawable.king_dark
        PieceType.QUEEN_DARK -> R.drawable.queen_dark
        PieceType.ROOK_DARK -> R.drawable.rook_dark
        PieceType.BISHOP_DARK -> R.drawable.bishop_dark
        PieceType.KNIGHT_DARK -> R.drawable.knight_dark
        PieceType.PAWN_DARK -> R.drawable.pawn_dark
    }

private fun getPieceTypeDescriptionRes(pieceType: PieceType): Int =
    when (pieceType) {
        PieceType.KING_LIGHT -> R.string.king_light
        PieceType.QUEEN_LIGHT -> R.string.queen_light
        PieceType.ROOK_LIGHT -> R.string.rook_light
        PieceType.BISHOP_LIGHT -> R.string.bishop_light
        PieceType.KNIGHT_LIGHT -> R.string.knight_light
        PieceType.PAWN_LIGHT -> R.string.pawn_light
        PieceType.KING_DARK -> R.string.king_dark
        PieceType.QUEEN_DARK -> R.string.queen_dark
        PieceType.ROOK_DARK -> R.string.rook_dark
        PieceType.BISHOP_DARK -> R.string.bishop_dark
        PieceType.KNIGHT_DARK -> R.string.knight_dark
        PieceType.PAWN_DARK -> R.string.pawn_dark
    }