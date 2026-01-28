package com.example.solitaire.ui

import androidx.lifecycle.ViewModel
import com.example.solitaire.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // 画面の状態を保持するFlow
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        resetGame()
    }

    // ゲームの初期化処理
    fun resetGame() {
        // 1. 全カード生成
        val allCards = Suit.values().flatMap { suit ->
            Rank.values().map { rank ->
                Card(suit = suit, rank = rank, isFaceUp = false)
            }
        }.shuffled() // シャッフル

        // 2. 場札(Tableau)への配布ロジック
        val mutableCards = allCards.toMutableList()
        val newTableau = ArrayList<List<Card>>()

        for (i in 0 until 7) {
            val columnCards = ArrayList<Card>()
            for (j in 0..i) {
                if (mutableCards.isNotEmpty()) {
                    val card = mutableCards.removeAt(0)
                    // 一番手前のカードだけ表にする
                    if (j == i) card.isFaceUp = true
                    columnCards.add(card)
                }
            }
            newTableau.add(columnCards)
        }

        // 3. 残りを山札(Stock)にする
        val newStock = mutableCards.toList()

        // 4. 状態を更新
        _gameState.update {
            it.copy(
                stock = newStock,
                waste = emptyList(),
                foundations = List(4) { emptyList() },
                tableau = newTableau
            )
        }
    }
}