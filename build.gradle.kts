plugins {
    id("com.android.application") version "8.2.0" apply false // ※バージョンは今のままでOK
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false // ★ここを 2.0.0 に変更
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false // ★この行を追加
}