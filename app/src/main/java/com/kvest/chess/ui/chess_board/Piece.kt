package com.kvest.chess.ui.chess_board

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.model.ChessBoard
import com.kvest.chess.ui.utils.toIntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val Z_INDEX_IDLE = 0f
private const val Z_INDEX_DRAGGING = 1f
private const val SCALE_IDLE = 1f
private const val SCALE_DRAGGING = 1.5f

@Stable
interface PieceListener {
    fun onTakePiece(square: Square)
    fun onReleasePiece(square: Square)
}

@Composable
fun Piece(
    piece: PieceOnSquare,
    squareSize: Dp,
    chessBoard: ChessBoard,
    listener: PieceListener,
    dragPieceOverSquareListener: (Square) -> Unit,
    modifier: Modifier = Modifier,
) {
    val squareSizePx = with(LocalDensity.current) {
        squareSize.toPx()
    }

    val row = chessBoard.getRow(piece.square)
    val column = chessBoard.getColumn(piece.square)

    val x by rememberUpdatedState(column * squareSizePx)
    val y by rememberUpdatedState(row * squareSizePx)
    var zIndex by remember { mutableStateOf(Z_INDEX_IDLE) }
    var scale by remember { mutableStateOf(SCALE_IDLE) }

    val offset = remember {
        Animatable(
            Offset(x = column * squareSizePx, y = row * squareSizePx),
            Offset.VectorConverter
        )
    }

    fun calculateSquare(): Square {
        val row = ((offset.value.y + squareSizePx / 2) / squareSizePx).toInt()
        val column = ((offset.value.x + squareSizePx / 2) / squareSizePx).toInt()
        return chessBoard[row, column]
    }

    LaunchedEffect(key1 = piece.square) {
        offset.animateTo(Offset(x, y))
    }

    PieceImage(
        pieceType = piece.pieceType,
        modifier = modifier
            .offset { offset.value.toIntOffset() }
            .zIndex(zIndex)
            .scale(scale)
            .size(squareSize)
            .pointerInput(Unit) {
                coroutineScope {
                    detectDragGestures(
                        onDragStart = {
                            zIndex = Z_INDEX_DRAGGING
                            scale = SCALE_DRAGGING

                            val square = calculateSquare()
                            listener.onTakePiece(square)
                        },
                        onDragEnd = {
                            zIndex = Z_INDEX_IDLE
                            scale = SCALE_IDLE

                            dragPieceOverSquareListener(Square.NONE)

                            val square = calculateSquare()
                            listener.onReleasePiece(square)

                            /**
                            Note: This is ugly hack. Animate this piece to it's original position
                            If the position of the piece will be changed by onReleasePiece then recomposion will happen,
                            this animation will be canceled and this piece will be animated to the center of the square.
                            If the move of this piece to the square is impossible - no recomposion will happen and
                            this piece will be moved to the original position.
                            This approach has potential bug - if the new state in onReleasePiece will be calculated too long -
                            the user will see a wrong animation
                             **/

                            /**
                            Note: This is ugly hack. Animate this piece to it's original position
                            If the position of the piece will be changed by onReleasePiece then recomposion will happen,
                            this animation will be canceled and this piece will be animated to the center of the square.
                            If the move of this piece to the square is impossible - no recomposion will happen and
                            this piece will be moved to the original position.
                            This approach has potential bug - if the new state in onReleasePiece will be calculated too long -
                            the user will see a wrong animation
                             **/
                            launch {
                                offset.animateTo(Offset(x, y))
                            }
                        },
                        onDragCancel = {
                            zIndex = Z_INDEX_IDLE
                            scale = SCALE_IDLE

                            dragPieceOverSquareListener(Square.NONE)

                            // Just move piece to it's original position
                            launch {
                                offset.animateTo(Offset(x, y))
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()

                        dragPieceOverSquareListener(calculateSquare())

                        launch {
                            offset.snapTo(
                                Offset(
                                    x = offset.value.x + dragAmount.x * scale,
                                    y = offset.value.y + dragAmount.y * scale,
                                )
                            )
                        }
                    }
                }
            }
    )
}
