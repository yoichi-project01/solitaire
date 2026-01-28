package com.example.solitaire.model

// ゲーム全体の状況を保持するデータクラス
data class GameState(
    val stock: List<Card> = emptyList(),        // 山札
    val waste: List<Card> = emptyList(),        // 捨て札
    val foundations: List<List<Card>> = List(4) { emptyList() }, // 組札(ゴール) x4
    val tableau: List<List<Card>> = List(7) { emptyList() }      // 場札 x7
)