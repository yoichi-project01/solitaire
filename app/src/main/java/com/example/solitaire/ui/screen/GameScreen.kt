package com.example.solitaire.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solitaire.ui.GameViewModel
import com.example.solitaire.ui.components.CardView

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()

    val greenBackground = Color(0xFF388E3C)
    val emptySlotColor = Color.White.copy(alpha = 0.3f)

    // カードの縦横比
    val cardAspectRatio = 2.0f / 3.0f

    // 全体を Row（横並び）レイアウト
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(greenBackground)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // ------------------------------------------------
        // 左エリア: 山札 & 捨て札 (縦並び)
        // ------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 山札 ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(cardAspectRatio)
            ) {
                if (gameState.stock.isNotEmpty()) {
                    CardView(
                        card = gameState.stock.last(),
                        onClick = { viewModel.onStockClicked() }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, emptySlotColor, RoundedCornerShape(2.dp))
                            .clickable { viewModel.onStockClicked() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("↺", color = emptySlotColor, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- 捨て札 ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(cardAspectRatio)
            ) {
                if (gameState.waste.isNotEmpty()) {
                    val card = gameState.waste.last()
                    CardView(
                        card = card,
                        onClick = { viewModel.onCardClicked(card, "Waste", 0) }
                    )
                }
            }
        }

        // ------------------------------------------------
        // 中央エリア: 場札 x 7列 (横並び)
        // ------------------------------------------------
        Row(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            gameState.tableau.forEachIndexed { index, pile ->
                Box(modifier = Modifier.weight(1f)) {
                    // 列全体をラップするBox
                    Box(modifier = Modifier.fillMaxSize()) {

                        // 空枠（カードがない時用）
                        if (pile.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(cardAspectRatio)
                                    .border(1.dp, emptySlotColor, RoundedCornerShape(2.dp))
                            )
                        }

                        // ★★★ スマートな重なり配置 ★★★
                        // Columnではなく、Boxの中でpaddingを使って位置を計算します
                        pile.forEachIndexed { cardIndex, card ->

                            // このカードより前にある「裏向き」と「表向き」の枚数を数える
                            val previousCards = pile.take(cardIndex)
                            val faceDownCount = previousCards.count { !it.isFaceUp }
                            val faceUpCount = previousCards.count { it.isFaceUp }

                            // ズラす量を計算
                            // 裏向き: 6dpずつ（ほぼ重なる）
                            // 表向き: 20dpずつ（数字が見える程度）
                            val topOffset = (faceDownCount * 6).dp + (faceUpCount * 20).dp

                            Box(
                                modifier = Modifier
                                    .padding(top = topOffset) // ここで位置を決定
                                    .fillMaxWidth()
                                    .aspectRatio(cardAspectRatio)
                            ) {
                                CardView(
                                    card = card,
                                    onClick = { viewModel.onCardClicked(card, "Tableau", index) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ------------------------------------------------
        // 右エリア: 組札 x 4 (縦並び)
        // ------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suitIcons = listOf("♥", "♦", "♣", "♠")
            gameState.foundations.forEachIndexed { index, pile ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(cardAspectRatio),
                    contentAlignment = Alignment.Center
                ) {
                    if (pile.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(2.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = suitIcons[index],
                                fontSize = 10.sp,
                                color = emptySlotColor
                            )
                        }
                    } else {
                        CardView(card = pile.last())
                    }
                }
            }
        }
    }
}