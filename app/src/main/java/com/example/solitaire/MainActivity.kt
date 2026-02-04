package com.example.solitaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat // ★追加
import androidx.core.view.WindowInsetsCompat // ★追加
import androidx.core.view.WindowInsetsControllerCompat // ★追加
import com.example.solitaire.ui.screen.GameScreen
import com.example.solitaire.ui.theme.SolitaireTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ▼▼▼ ここから追加：全画面表示（ステータスバー・ナビゲーションバー非表示）の設定 ▼▼▼
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // バーを出したいときはスワイプで一時的に表示する設定
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // ステータスバー（上）とナビゲーションバー（下）を隠す
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // ▲▲▲ ここまで追加 ▲▲▲

        setContent {
            SolitaireTheme {
                GameScreen()
            }
        }
    }
}