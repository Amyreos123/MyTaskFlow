package com.example.mytaskflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// --- ИЗМЕНЕНИЕ ---
// 1. Увеличиваем версию с 2 до 3.
// 2. Добавляем SubTask::class в список entities.
@Database(entities = [Task::class, SubTask::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    // --- НОВОЕ ---
    // 3. Добавляем DAO для подзадач
    abstract fun subTaskDao(): SubTaskDao
    // --- КОНЕЦ НОВОГО ---

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        // Миграция с 1 на 2 (добавление 'priority')
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE tasks ADD COLUMN priority INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        // --- НОВОЕ ---
        // 4. Определяем нашу вторую миграцию (с версии 2 на 3).
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Это SQL-команда: "СОЗДАТЬ ТАБЛИЦУ sub_tasks..."
                // Мы дублируем здесь схему из data class SubTask,
                // включая внешний ключ (FOREIGN KEY)
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `sub_tasks` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `taskId` INTEGER NOT NULL,
                        `title` TEXT NOT NULL,
                        `isCompleted` INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(`taskId`) REFERENCES `tasks`(`id`) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                // Создаем "индекс" для 'taskId' - это ускорит поиск подзадач
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_tasks_taskId` ON `sub_tasks`(`taskId`)")
            }
        }
        // --- КОНЕЦ НОВОГО ---

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "task_database"
                )
                    // --- ИЗМЕНЕНИЕ ---
                    // 5. Добавляем НОВУЮ миграцию в список
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    // --- КОНЕЦ ИЗМЕНЕНИЯ ---
                    .build()
                    .also { Instance = it }
            }
        }
    }
}