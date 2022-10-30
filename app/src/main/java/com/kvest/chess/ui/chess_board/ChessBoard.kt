package com.kvest.chess.ui.chess_board

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.bhlangonijr.chesslib.Square
import com.kvest.chess.R
import com.kvest.chess.model.*
import com.kvest.chess.ui.theme.Copper

@Composable
fun ChessBoard(
    board: BoardModel,
    onCellClicked: (Square) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .border(2.dp, color = Color.Black)
    ) {
        Column {
            board.rows.map { row ->
                Row {
                    row.cells.map { cell ->
                        ChessBoardCell(cell, onCellClicked)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChessBoardCell(
    cell: Cell,
    onCellClicked: (Square) -> Unit,
    modifier: Modifier = Modifier
) {
    val cellColor = when (cell.type) {
        CellType.DARK -> Copper
        CellType.LIGHT -> Color.White
    }

    Box(
        modifier = modifier
            .background(cellColor)
            .width(42.dp)
            .height(42.dp)
            .clickable {
                onCellClicked(cell.square)
            },
        contentAlignment = Alignment.Center
    ) {
        if (cell.isSelected) {
            Surface(
                color = Color.Yellow.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxSize()
            ) {}
        }
        if (cell.isForMove) {
            Surface(
                color = Color.Black.copy(alpha = 0.3f),
                shape = CircleShape,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {}
        }
        if (cell.piece != null) {
            Image(
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(getPieceTypeImageRes(cell.piece)),
                contentDescription = stringResource(getPieceTypeDescriptionRes(cell.piece))
            )
        }
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

@Preview
@Composable
fun ChessBoardPreview() {
    val rows = (0..7).map { rowId ->
        BoardRow(
            (0..7).map { columnId ->
                Cell(
                    Square.A1,
                    if ((rowId + columnId) % 2 == 0) CellType.LIGHT else CellType.DARK,
                    piece = null,
                    isSelected = false,
                    isForMove = false,
                )
            }
        )
    }

    ChessBoard(
        BoardModel(rows),
        {}
    )
}

@Preview
@Composable
fun ChessBoardCellPreview() {
    Row {
        ChessBoardCell(
            Cell(
                Square.A1,
                CellType.DARK,
                PieceType.BISHOP_DARK,
                isSelected = true,
                isForMove = false
            ), {})
        ChessBoardCell(
            Cell(
                Square.A1,
                CellType.LIGHT,
                PieceType.KING_LIGHT,
                isSelected = true,
                isForMove = false
            ), {})
        ChessBoardCell(
            Cell(Square.A1, CellType.DARK, null, isSelected = false, isForMove = true),
            {})
        ChessBoardCell(
            Cell(Square.A1, CellType.LIGHT, null, isSelected = false, isForMove = false),
            {})
    }
}

@Preview
@Composable
fun ChessBoardAllPiecesPreview() {
    Row {
        PieceType.values().map {
            ChessBoardCell(
                Cell(Square.A1, CellType.DARK, it, isSelected = false, isForMove = false),
                {}
            )
        }
    }
}