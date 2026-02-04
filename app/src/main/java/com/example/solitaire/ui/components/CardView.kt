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
import androidx.compose.ui.text.style.TextAlign
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
            .clip(shape)
            .background(if (card.isFaceUp) Color.White else Color.Transparent)
            .background(
                if (!card.isFaceUp) cardBackBrush
                else SolidColor(Color.Transparent)
            )
            .border(0.5.dp, Color(0xFFE0E0E0), shape)
            .clickable { onClick() }
            // ★変更: 余白を極小のまま維持（文字スペース確保のため）
            .padding(1.dp)
    ) {
        if (card.isFaceUp) {
            // --- 表向きのデザイン ---

            // 左上の数字とマーク
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 1.dp, top = 0.dp), // 位置微調整
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    // ★変更: 6.sp -> 11.sp (倍近く大きく)
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    // ★変更: 6.sp -> 11.sp
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    textAlign = TextAlign.Center
                )
            }

            // 中央の大きなマーク
            Text(
                text = card.suit.label,
                color = card.suit.color,
                // ★変更: 12.sp -> 22.sp (大きく目立たせる)
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.Center)
            )

            // 右下の数字とマーク (逆さま)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 1.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    // ★変更: 6.sp -> 11.sp
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    // ★変更: 6.sp -> 11.sp
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // --- 裏向きのデザイン ---
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(8.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
            )
        }
    }
}