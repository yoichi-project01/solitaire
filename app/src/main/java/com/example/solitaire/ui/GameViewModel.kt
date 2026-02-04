package com.example.solitaire.ui

import androidx.lifecycle.ViewModel
import com.example.solitaire.logic.SolitaireRules
import com.example.solitaire.model.Card
import com.example.solitaire.model.GameState
import com.example.solitaire.model.Rank
import com.example.solitaire.model.Suit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        resetGame()
    }

    fun resetGame() {
        val allCards = Suit.values().flatMap { suit ->
            Rank.values().map { rank ->
                Card(suit = suit, rank = rank, isFaceUp = false)
            }
        }.shuffled()

        val mutableCards = allCards.toMutableList()
        val newTableau = ArrayList<List<Card>>()

        for (i in 0 until 7) {
            val columnCards = ArrayList<Card>()
            for (j in 0..i) {
                if (mutableCards.isNotEmpty()) {
                    val card = mutableCards.removeAt(0)
                    if (j == i) card.isFaceUp = true
                    columnCards.add(card)
                }
            }
            newTableau.add(columnCards)
        }

        _gameState.update {
            it.copy(
                stock = mutableCards.toList(),
                waste = emptyList(),
                foundations = List(4) { emptyList() },
                tableau = newTableau
            )
        }
    }

    fun onStockClicked() {
        _gameState.update { currentState ->
            val newStock = currentState.stock.toMutableList()
            val newWaste = currentState.waste.toMutableList()

            if (newStock.isNotEmpty()) {
                val card = newStock.removeLast()
                val flippedCard = card.copy(isFaceUp = true)
                newWaste.add(flippedCard)
            } else {
                newStock.addAll(newWaste.reversed().map { it.copy(isFaceUp = false) })
                newWaste.clear()
            }

            currentState.copy(stock = newStock, waste = newWaste)
        }
    }

    // タップ移動用（既存）
    fun onCardClicked(card: Card, fromPileType: String, pileIndex: Int) {
        val currentState = _gameState.value

        // 1. 組札への移動チェック
        for (i in 0 until 4) {
            val foundationPile = currentState.foundations[i]
            if (SolitaireRules.canMoveToFoundation(card, foundationPile)) {
                moveCardToFoundation(card, fromPileType, pileIndex, i)
                return
            }
        }

        // 2. 場札への移動チェック
        for (i in 0 until 7) {
            if (fromPileType == "Tableau" && pileIndex == i) continue

            val tableauPile = currentState.tableau[i]
            if (SolitaireRules.canMoveToTableau(card, tableauPile)) {
                moveCardToTableau(card, fromPileType, pileIndex, i)
                return
            }
        }
    }

    // ★追加: ドラッグ＆ドロップ用（指定された場所に移動を試みる）
    fun onCardDropped(card: Card, fromType: String, fromIndex: Int, targetType: String, targetIndex: Int) {
        val currentState = _gameState.value

        if (targetType == "Foundation") {
            // 組札へのドロップ
            val foundationPile = currentState.foundations[targetIndex]
            if (SolitaireRules.canMoveToFoundation(card, foundationPile)) {
                moveCardToFoundation(card, fromType, fromIndex, targetIndex)
            }
        } else if (targetType == "Tableau") {
            // 場札へのドロップ
            val tableauPile = currentState.tableau[targetIndex]
            // 同じ列へのドロップは無視
            if (fromType == "Tableau" && fromIndex == targetIndex) return

            if (SolitaireRules.canMoveToTableau(card, tableauPile)) {
                moveCardToTableau(card, fromType, fromIndex, targetIndex)
            }
        }
    }

    private fun moveCardToFoundation(card: Card, fromType: String, fromIndex: Int, toIndex: Int) {
        _gameState.update { state ->
            val newStock = state.stock.toMutableList()
            val newWaste = state.waste.toMutableList()
            val newFoundations = state.foundations.map { it.toMutableList() }.toMutableList()
            val newTableau = state.tableau.map { it.toMutableList() }.toMutableList()

            removeFromSource(card, fromType, fromIndex, newStock, newWaste, newTableau)
            newFoundations[toIndex].add(card)

            state.copy(
                stock = newStock,
                waste = newWaste,
                foundations = newFoundations,
                tableau = newTableau
            )
        }
    }

    private fun moveCardToTableau(card: Card, fromType: String, fromIndex: Int, toIndex: Int) {
        _gameState.update { state ->
            val newStock = state.stock.toMutableList()
            val newWaste = state.waste.toMutableList()
            val newFoundations = state.foundations.map { it.toMutableList() }.toMutableList()
            val newTableau = state.tableau.map { it.toMutableList() }.toMutableList()

            removeFromSource(card, fromType, fromIndex, newStock, newWaste, newTableau)
            newTableau[toIndex].add(card)

            state.copy(
                stock = newStock,
                waste = newWaste,
                foundations = newFoundations,
                tableau = newTableau
            )
        }
    }

    private fun removeFromSource(
        card: Card,
        type: String,
        index: Int,
        stock: MutableList<Card>,
        waste: MutableList<Card>,
        tableau: MutableList<MutableList<Card>>
    ) {
        when (type) {
            "Waste" -> waste.remove(card)
            "Tableau" -> {
                val column = tableau[index]
                column.remove(card)
                if (column.isNotEmpty() && !column.last().isFaceUp) {
                    val lastCard = column.last()
                    column[column.lastIndex] = lastCard.copy(isFaceUp = true)
                }
            }
        }
    }
}