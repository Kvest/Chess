package com.kvest.chess.ui.chess_board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kvest.chess.model.PieceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PromotionPane(
    promotions: ImmutableList<PieceType>,
    cellSize: Dp,
    onPromotionPieceTypeSelected: (PieceType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val itemModifier = Modifier
            .size(cellSize)
            .padding(6.dp)

        promotions.forEach { pieceType ->
            PieceImage(
                pieceType = pieceType,
                modifier = itemModifier
                    .clickable { onPromotionPieceTypeSelected(pieceType) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
fun PromotionPanePreview() {
    PromotionPane(
        promotions = listOf(
            PieceType.QUEEN_DARK,
            PieceType.QUEEN_LIGHT,
            PieceType.ROOK_DARK,
            PieceType.KNIGHT_LIGHT
        ).toImmutableList(),
        cellSize = 78.dp,
        onPromotionPieceTypeSelected = {},
        modifier = Modifier
            .background(Color.White)
            .border(2.dp, color = Color.DarkGray)
    )
}