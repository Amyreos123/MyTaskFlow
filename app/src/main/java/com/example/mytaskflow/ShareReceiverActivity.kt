package com.example.mytaskflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytaskflow.data.HubItem
import com.example.mytaskflow.data.MyTaskFlowApplication
import kotlinx.coroutines.launch

class ShareReceiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем, что к нам пришел нужный Intent
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleSharedText(intent)
        } else {
            // Если что-то пошло не так, просто закрываемся
            finish()
        }
    }

    private fun handleSharedText(intent: Intent) {
        // Получаем репозиторий из нашего Application класса
        val hubRepository = (application as MyTaskFlowApplication).hubRepository

        // Извлекаем текст (URL)
        val sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (!sharedUrl.isNullOrBlank()) {
            // Запускаем корутину для сохранения в БД
            lifecycleScope.launch {
                try {
                    // Создаем HubItem.
                    // В качестве "title" пока просто используем сам URL.
                    // Позже, на шаге полировки, мы могли бы научиться извлекать
                    // настоящий <title> веб-страницы, но это сложно
                    // (требует сетевого запроса), поэтому сейчас упростим.
                    val newItem = HubItem(
                        url = sharedUrl,
                        title = sharedUrl, // Используем URL как временный заголовок
                        timestamp = System.currentTimeMillis()
                    )

                    hubRepository.insert(newItem)

                    // Показываем пользователю, что все удалось
                    Toast.makeText(
                        applicationContext,
                        "Ссылка сохранена в Хаб",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Не удалось сохранить ссылку",
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    // В любом случае (успех или ошибка) закрываем Activity
                    finish()
                }
            }
        } else {
            // Если текст пустой, закрываемся
            Toast.makeText(
                applicationContext,
                "Нет ссылки для сохранения",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}