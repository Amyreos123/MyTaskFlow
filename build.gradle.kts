// Файл: build.gradle.kts (в корне проекта)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // --- ИСПРАВЛЕНИЕ ---
    // Теперь этот alias (псевдоним) существует в libs.versions.toml,
    // и Gradle найдет его.
    alias(libs.plugins.ksp) apply false
    // --- КОНЕЦ ИСПРАВЛЕНИЯ ---
}