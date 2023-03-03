package com.kvest.chess.ui.chess_board

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kvest.chess.R
import com.kvest.chess.model.PieceType

@Composable
fun PieceImage(
    pieceType: PieceType,
    modifier: Modifier,
) {
    Image(
        modifier = modifier,
        contentScale = ContentScale.Fit,
        painter = painterResource(getPieceTypeImageRes(pieceType)),
        contentDescription = stringResource(getPieceTypeDescriptionRes(pieceType))
    )
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

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
fun PiecePieceImage() {
    Row {
        PieceImage(
            pieceType = PieceType.BISHOP_DARK,
            modifier = Modifier.size(48.dp)
        )
        PieceImage(
            pieceType = PieceType.PAWN_LIGHT,
            modifier = Modifier.size(48.dp)
        )
    }
}