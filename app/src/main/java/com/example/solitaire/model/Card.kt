package com.example.solitaire.model

import java.util.UUID

data class Card(
    val id: String = UUID.randomUUID().toString(),
    val suit: Suit,
    val rank: Rank,
    var isFaceUp: Boolean = false // 表向きかどうか
)