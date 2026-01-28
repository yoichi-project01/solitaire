package com.example.solitaire.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solitaire.model.Card

@Composable
fun CardView(
    card: Card,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(1.dp)

    val cardBackBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4285F4), Color(0xFF1967D2))
    )

    Box(
        modifier = modifier
            // ★変更: 少し細長い比率に変更 (横幅を狭くする)
            .aspectRatio(2.2f / 3.5f)
            .clip(shape)
            .background(if (card.isFaceUp) Color.White else Color.Transparent)
            .background(
                if (!card.isFaceUp) cardBackBrush
                else SolidColor(Color.Transparent)
            )
            .border(0.5.dp, Color(0xFFE0E0E0), shape)
            .clickable { onClick() }
            // ★変更: パディングを0.5dpへ極小化
            .padding(0.5.dp)
    ) {
        if (card.isFaceUp) {
            // --- 表向きのデザイン ---

            // 左上の数字とマーク
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    // ★変更: フォントサイズをさらに小さく (8sp -> 7sp)
                    fontSize = 7.sp
                )
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    // ★変更: (8sp -> 7sp)
                    fontSize = 7.sp
                )
            }

            // 中央の大きなマーク
            Text(
                text = card.suit.label,
                color = card.suit.color,
                // ★変更: (16sp -> 14sp)
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center)
            )

            // 右下の数字とマーク
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    // ★変更: (8sp -> 7sp)
                    fontSize = 7.sp
                )
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    // ★変更: (8sp -> 7sp)
                    fontSize = 7.sp
                )
            }
        } else {
            // --- 裏向きのデザイン ---
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    // ★変更: 裏面の模様も小さく (8dp -> 6dp)
                    .size(6.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
            )
        }
    }
}