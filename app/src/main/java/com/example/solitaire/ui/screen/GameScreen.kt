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

    // ★ 変更点1: 全体を Column（縦並び）に変更
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(greenBackground)
            .padding(8.dp)
    ) {
        // ------------------------------------------------
        // 上段エリア: [山札] [捨て札] ... [組札1] [組札2] [組札3] [組札4]
        // ------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f), // 画面の高さの約1.5割
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // --- 山札 (Stock) ---
            Box(
                modifier = Modifier
                    .weight(1f) // 幅を均等割
                    .aspectRatio(2.5f / 3.5f)
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
                            .border(2.dp, emptySlotColor, RoundedCornerShape(4.dp))
                            .clickable { viewModel.onStockClicked() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("↺", color = emptySlotColor, fontSize = 24.sp)
                    }
                }
            }

            // --- 捨て札 (Waste) ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(2.5f / 3.5f)
            ) {
                if (gameState.waste.isNotEmpty()) {
                    val card = gameState.waste.last()
                    CardView(
                        card = card,
                        onClick = { viewModel.onCardClicked(card, "Waste", 0) }
                    )
                }
            }

            // --- スペーサー（隙間） ---
            // 山札・捨て札と、右側の組札の間を少し空ける
            Spacer(modifier = Modifier.weight(0.5f))

            // --- 組札 (Foundations) x 4 ---
            val suitIcons = listOf("♥", "♦", "♣", "♠")
            gameState.foundations.forEachIndexed { index, pile ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(2.5f / 3.5f),
                    contentAlignment = Alignment.Center
                ) {
                    if (pile.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = suitIcons[index],
                                fontSize = 20.sp,
                                color = emptySlotColor
                            )
                        }
                    } else {
                        CardView(card = pile.last())
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------------------------------------
        // 下段エリア: 場札 (Tableau) x 7列
        // ------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f), // 残りのスペースを大きく使う
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            gameState.tableau.forEachIndexed { index, pile ->
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        // 空枠
                        if (pile.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2.5f / 3.5f)
                                    .border(1.dp, emptySlotColor, RoundedCornerShape(4.dp))
                            )
                        }

                        // カードの重なり
                        pile.forEachIndexed { cardIndex, card ->
                            Box(
                                modifier = Modifier
                                    .padding(top = (cardIndex * 25).dp) // 少し詰め気味に重ねる
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
    }
}