package com.example.mytaskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.mytaskflow.ui.theme.MyTaskFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // MyTaskFlowTheme - это наша настроенная тема (из ui/theme/Theme.kt)
            // Она использует Typography, которую мы определили в Type.kt
            MyTaskFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Просто разместим текст по центру для проверки
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            // Мы используем здесь app_name, чтобы проверить и strings.xml
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium, // Используем стиль из нашей темы
                            fontWeight = FontWeight.Bold // Проверим жирное начертание
                        )
                    }
                }
            }
        }
    }
}