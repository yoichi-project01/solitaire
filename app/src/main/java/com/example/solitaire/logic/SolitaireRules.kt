package com.example.solitaire.logic

import com.example.solitaire.model.Card
import com.example.solitaire.model.Rank

object SolitaireRules {

    // 場札(Tableau)にカードを置けるか判定する
    fun canMoveToTableau(cardToMove: Card, targetPile: List<Card>): Boolean {
        if (targetPile.isEmpty()) {
            // 空の列にはキング(K)のみ置ける
            return cardToMove.rank == Rank.KING
        }

        val targetCard = targetPile.last()

        // ルール1: 色が違うこと（赤と黒）
        val isDifferentColor = (cardToMove.suit.color != targetCard.suit.color)

        // ルール2: 数字が1つ小さいこと (例: 7の上に6)
        val isOneRankLower = (cardToMove.rank.value == targetCard.rank.value - 1)

        return isDifferentColor && isOneRankLower
    }

    // ▼▼▼ この関数が見つからないエラーが出ていました ▼▼▼
    // 組札(Foundation/ゴール)にカードを置けるか判定する
    fun canMoveToFoundation(cardToMove: Card, foundationPile: List<Card>): Boolean {
        if (foundationPile.isEmpty()) {
            // 最初はエース(A)のみ置ける
            return cardToMove.rank == Rank.ACE
        }

        val targetCard = foundationPile.last()

        // ルール1: 同じマークであること
        val isSameSuit = (cardToMove.suit == targetCard.suit)

        // ルール2: 数字が1つ大きいこと (例: Aの上に2)
        val isOneRankHigher = (cardToMove.rank.value == targetCard.rank.value + 1)

        return isSameSuit && isOneRankHigher
    }
}