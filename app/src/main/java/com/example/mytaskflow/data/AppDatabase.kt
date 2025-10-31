package com.example.mytaskflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Task::class, SubTask::class, Habit::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        // --- ИЗМЕНЕНИЕ ---
        // Миграция с 1 на 2 (добавление 'priority' в Task)
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) { // Было 'database'
                db.execSQL( // Было 'database'
                    "ALTER TABLE tasks ADD COLUMN priority INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        // Миграция с 2 на 3 (добавление таблицы 'sub_tasks')
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) { // Было 'database'
                db.execSQL( // Было 'database'
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
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_tasks_taskId` ON `sub_tasks`(`taskId`)") // Было 'database'
            }
        }

        // Миграция с 3 на 4 (добавление таблицы 'habits')
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) { // Было 'database'
                db.execSQL( // Было 'database'
                    """
                    CREATE TABLE IF NOT EXISTS `habits` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `completedDates` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
        // --- КОНЕЦ ИЗМЕНЕНИЙ ---

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "task_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}