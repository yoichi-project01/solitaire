package com.example.solitaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.solitaire.ui.screen.GameScreen
import com.example.solitaire.ui.theme.SolitaireTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ここでエラーが消えるはずです
        setContent {
            SolitaireTheme {
                GameScreen()
            }
        }
    }
}