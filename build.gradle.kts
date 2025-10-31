plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // --- ВОТ ЭТА СТРОКА БЫЛА ПРОПУЩЕНА ---
    alias(libs.plugins.kotlin.compose) apply false
}