package com.example.mytaskflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// --- ИЗМЕНЕНИЕ ---
// 1. Увеличиваем версию с 3 до 4.
// 2. Добавляем Habit::class в список entities.
// 3. Добавляем аннотацию @TypeConverters
@Database(entities = [Task::class, SubTask::class, Habit::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    // --- НОВОЕ ---
    // 4. Добавляем DAO для привычек
    abstract fun habitDao(): HabitDao
    // --- КОНЕЦ НОВОГО ---

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        // Миграция с 1 на 2 (добавление 'priority' в Task)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE tasks ADD COLUMN priority INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        // Миграция с 2 на 3 (добавление таблицы 'sub_tasks')
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
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
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_tasks_taskId` ON `sub_tasks`(`taskId`)")
            }
        }

        // --- НОВОЕ ---
        // 5. Определяем нашу третью миграцию (с версии 3 на 4).
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Это SQL-команда: "СОЗДАТЬ ТАБЛИЦУ habits..."
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `habits` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `completedDates` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                // P.S. В 'completedDates' будет храниться String
                // (результат работы нашего TypeConverter'а)
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
                    // 6. Добавляем НОВУЮ миграцию MIGRATION_3_4 в список
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    // --- КОНЕЦ ИЗМЕНЕНИЯ ---
                    .build()
                    .also { Instance = it }
            }
        }
    }
}