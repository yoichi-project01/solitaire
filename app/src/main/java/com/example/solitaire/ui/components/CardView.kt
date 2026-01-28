package com.example.solitaire.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solitaire.model.Card

@Composable
fun CardView(
    card: Card,
    modifier: Modifier = Modifier
) {
    // カードの形状定義
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .aspectRatio(2.5f / 3.5f) // トランプの縦横比
            .clip(shape)
            .background(if (card.isFaceUp) Color.White else Color(0xFF1E88E5)) // 表は白、裏は青
            .border(1.dp, Color.Black, shape)
            .padding(4.dp)
    ) {
        if (card.isFaceUp) {
            // 表向きのデザイン（簡易版）
            Box(modifier = Modifier.fillMaxSize()) {
                // 左上の数字
                Text(
                    text = "${card.rank.label}\n${card.suit.label}",
                    color = card.suit.color,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                // 中央の大きなマーク
                Text(
                    text = card.suit.label,
                    color = card.suit.color,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            // 裏向きのデザイン（必要なら模様などを描画）
        }
    }
}