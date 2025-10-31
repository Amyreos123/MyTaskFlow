package com.example.mytaskflow.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytaskflow.data.HubItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HubScreen(
    modifier: Modifier = Modifier,
    // 1. Внедряем нашу ViewModel
    hubViewModel: HubViewModel = viewModel(factory = HubViewModel.Factory)
) {
    // 2. Подписываемся на StateFlow со списком всех элементов
    val hubItems by hubViewModel.allItems.collectAsStateWithLifecycle()

    // 3. Проверяем, пуст ли список
    if (hubItems.isEmpty()) {
        // Показываем заглушку, если список пуст
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Хаб пуст. Поделитесь ссылкой!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        // 4. Показываем список, если в нем есть элементы
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(hubItems) { item ->
                HubItemCard(item = item)
            }
        }
    }
}

/**
 * Отдельный Composable для отображения одной карточки ссылки.
 */
@Composable
fun HubItemCard(item: HubItem) {
    val context = LocalContext.current
    // Вспомогательная функция для форматирования даты
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val formattedDate = sdf.format(Date(item.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 5. Открываем ссылку в браузере по клику
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Обработка, если нет браузера или ссылка некорректна
                    e.printStackTrace()
                }
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Временно используем URL как заголовок, как и в ShareReceiverActivity
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.url,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary // Выделим ссылку цветом
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}