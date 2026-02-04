package com.example.solitaire.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solitaire.model.Card
import com.example.solitaire.ui.GameViewModel
import com.example.solitaire.ui.components.CardView
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()

    val greenBackground = Color(0xFF388E3C)
    val emptySlotColor = Color.White.copy(alpha = 0.3f)
    val cardAspectRatio = 2.0f / 2.9f

    // --- ドラッグ＆ドロップ用の状態管理 ---
    var draggingCard by remember { mutableStateOf<Card?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    // ★追加: ドラッグ開始時の「カードの絶対座標」と「サイズ」を記録する変数
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }
    var dragCardSize by remember { mutableStateOf(IntSize.Zero) }

    // ドラッグ中のカードを描画するためのBox（全画面）
    Box(modifier = Modifier.fillMaxSize()) {

        // ------------------------------------------------
        // メインのゲーム画面
        // ------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(greenBackground)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            // 1. 左エリア: 山札 & 捨て札
            Column(
                modifier = Modifier.weight(0.5f).fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 山札
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(cardAspectRatio)) {
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
                            Text("↺", color = emptySlotColor, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 捨て札 (ドラッグ可能)
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(cardAspectRatio)) {
                    if (gameState.waste.isNotEmpty()) {
                        val card = gameState.waste.last()
                        val isBeingDragged = (draggingCard == card)

                        // ★このカードの現在位置とサイズを保持する変数
                        var currentPosition by remember { mutableStateOf(Offset.Zero) }
                        var currentSize by remember { mutableStateOf(IntSize.Zero) }

                        Box(
                            modifier = Modifier
                                .alpha(if (isBeingDragged) 0f else 1f)
                                // ★座標とサイズを取得
                                .onGloballyPositioned { coordinates ->
                                    currentPosition = coordinates.positionInRoot()
                                    currentSize = coordinates.size
                                }
                                .pointerInput(card) {
                                    detectDragGestures(
                                        onDragStart = {
                                            draggingCard = card
                                            // ★ドラッグ開始時の位置を記録
                                            dragStartPosition = currentPosition
                                            dragCardSize = currentSize
                                            dragOffset = Offset.Zero
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                        },
                                        onDragEnd = {
                                            viewModel.onCardClicked(card, "Waste", 0)
                                            draggingCard = null
                                            dragOffset = Offset.Zero
                                        },
                                        onDragCancel = {
                                            draggingCard = null
                                            dragOffset = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            CardView(card = card)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // 2. 中央エリア: 場札
            Row(
                modifier = Modifier.weight(5.0f).fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                gameState.tableau.forEachIndexed { index, pile ->
                    Box(modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // 空枠
                            if (pile.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(cardAspectRatio)
                                        .border(1.dp, emptySlotColor, RoundedCornerShape(2.dp))
                                        .clickable {
                                            // 簡易ドロップ用
                                        }
                                )
                            }

                            // カード一覧
                            pile.forEachIndexed { cardIndex, card ->
                                val previousCards = pile.take(cardIndex)
                                val faceDownCount = previousCards.count { !it.isFaceUp }
                                val faceUpCount = previousCards.count { it.isFaceUp }
                                val topOffset = (faceDownCount * 6).dp + (faceUpCount * 20).dp

                                val isBeingDragged = (draggingCard == card)
                                val isDraggable = card.isFaceUp && card == pile.last()

                                // ★このカードの現在位置とサイズを保持する変数
                                var currentPosition by remember { mutableStateOf(Offset.Zero) }
                                var currentSize by remember { mutableStateOf(IntSize.Zero) }

                                Box(
                                    modifier = Modifier
                                        .padding(top = topOffset)
                                        .fillMaxWidth()
                                        .aspectRatio(cardAspectRatio)
                                        .alpha(if (isBeingDragged) 0f else 1f)
                                        // ★座標とサイズを取得
                                        .onGloballyPositioned { coordinates ->
                                            currentPosition = coordinates.positionInRoot()
                                            currentSize = coordinates.size
                                        }
                                        .pointerInput(card) {
                                            if (isDraggable) {
                                                detectDragGestures(
                                                    onDragStart = {
                                                        draggingCard = card
                                                        // ★ドラッグ開始時の位置を記録
                                                        dragStartPosition = currentPosition
                                                        dragCardSize = currentSize
                                                        dragOffset = Offset.Zero
                                                    },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        dragOffset += dragAmount
                                                    },
                                                    onDragEnd = {
                                                        viewModel.onCardClicked(card, "Tableau", index)
                                                        draggingCard = null
                                                        dragOffset = Offset.Zero
                                                    },
                                                    onDragCancel = {
                                                        draggingCard = null
                                                        dragOffset = Offset.Zero
                                                    }
                                                )
                                            }
                                        }
                                ) {
                                    CardView(
                                        card = card,
                                        onClick = {
                                            viewModel.onCardClicked(card, "Tableau", index)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // 3. 右エリア: 組札
            Column(
                modifier = Modifier.weight(0.5f).fillMaxHeight(),
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
                                Text(text = suitIcons[index], fontSize = 10.sp, color = emptySlotColor)
                            }
                        } else {
                            CardView(card = pile.last())
                        }
                    }
                }
            }
        }

        // ------------------------------------------------
        // ★修正: ドラッグ中のカードレイヤー
        // ------------------------------------------------
        if (draggingCard != null) {
            // 現在のドラッグ位置 = 開始位置 + 移動量
            val currentDragX = dragStartPosition.x + dragOffset.x
            val currentDragY = dragStartPosition.y + dragOffset.y

            // ピクセルサイズをDPに変換してサイズを復元
            val density = LocalDensity.current
            val cardWidthDp = with(density) { dragCardSize.width.toDp() }
            val cardHeightDp = with(density) { dragCardSize.height.toDp() }

            Box(
                modifier = Modifier
                    // ★開始位置からのオフセットで配置（これでマウスに追従します）
                    .offset { IntOffset(currentDragX.roundToInt(), currentDragY.roundToInt()) }
                    .width(cardWidthDp)
                    .height(cardHeightDp)
            ) {
                CardView(card = draggingCard!!)
            }
        }
    }
}