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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solitaire.model.Card
import androidx.compose.ui.graphics.SolidColor

@Composable
fun CardView(
    card: Card,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // タップ操作を受け取れるように追加
) {
    val shape = RoundedCornerShape(4.dp)

    // 画像のような青色グラデーション（裏面用）
    val cardBackBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4285F4), Color(0xFF1967D2))
    )

    Box(
        modifier = modifier
            .aspectRatio(2.5f / 3.5f)
            .clip(shape)
            .background(if (card.isFaceUp) Color.White else Color.Transparent)
            .background(if (!card.isFaceUp) cardBackBrush else SolidColor(Color.Transparent))
            .border(1.dp, Color(0xFFE0E0E0), shape) // 薄い枠線
            .clickable { onClick() } // タップ機能
            .padding(4.dp)
    ) {
        if (card.isFaceUp) {
            // --- 表向きのデザイン ---

            // 左上の数字とマーク
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    fontSize = 14.sp
                )
            }

            // 中央の大きなマーク（絵柄の代わり）
            Text(
                text = card.suit.label,
                color = card.suit.color,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.Center)
            )

            // 右下の数字とマーク（180度回転させるとより本格的ですが今回は簡易的に配置）
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    fontSize = 14.sp
                )
                Text(
                    text = card.rank.label,
                    color = card.suit.color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        } else {
            // --- 裏向きのデザイン ---
            // 画像の「G」ロゴの代わりに白い円を描いておく
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
            )
        }
    }
}