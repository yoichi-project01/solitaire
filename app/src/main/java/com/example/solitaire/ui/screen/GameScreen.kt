package com.example.solitaire.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solitaire.ui.GameViewModel
import com.example.solitaire.ui.components.CardView

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()

    // 緑色の背景（ゲームマット）
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E7D32))
            .padding(8.dp)
    ) {
        // --- 上段エリア (山札、捨て札、組札) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // 画面縦幅の比率
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 山札 (Stock)
            Box(modifier = Modifier.weight(1f)) {
                if (gameState.stock.isNotEmpty()) {
                    // 山札は裏向き表示
                    CardView(card = gameState.stock.last())
                } else {
                    // 空の場合の枠線など
                }
            }

            // 捨て札 (Waste)
            Box(modifier = Modifier.weight(1f)) {
                if (gameState.waste.isNotEmpty()) {
                    CardView(card = gameState.waste.last())
                }
            }

            // スペーサー（空きスペース）
            Spacer(modifier = Modifier.weight(1f))

            // 組札 (Foundations) x 4
            gameState.foundations.forEach { foundationPile ->
                Box(modifier = Modifier.weight(1f)) {
                    if (foundationPile.isNotEmpty()) {
                        CardView(card = foundationPile.last())
                    } else {
                        // 空の枠（Aを置く場所）を表示してもよい
                        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.2f)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 下段エリア (場札 Tableau) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            gameState.tableau.forEach { pile ->
                Box(modifier = Modifier.weight(1f)) {
                    // 場札を少しずつずらして表示
                    Column {
                        pile.forEachIndexed { index, card ->
                            // カードを重ねて表示するために負のマージンを使う手もあるが、
                            // ここでは簡易的にオフセット付きで配置
                            Box(
                                modifier = Modifier
                                    .padding(top = (index * 25).dp) // ずらす量
                            ) {
                                CardView(card = card)
                            }
                        }
                    }
                }
            }
        }
    }
}