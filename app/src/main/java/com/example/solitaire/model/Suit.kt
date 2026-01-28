package com.example.solitaire.model

import androidx.compose.ui.graphics.Color

enum class Suit(val label: String, val color: Color) {
    HEARTS("♥", Color.Red),
    DIAMONDS("♦", Color.Red),
    CLUBS("♣", Color.Black),
    SPADES("♠", Color.Black)
}