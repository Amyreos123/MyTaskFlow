package com.example.mytaskflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Говорим Room, что это класс БД.
// 2. Указываем, какие 'Entities' (таблицы) он содержит.
// 3. 'version = 1' - это первая версия нашей БД. Если мы изменим структуру, нужно будет увеличить версию.
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 4. Указываем, какие DAO (пульты управления) использует эта БД.
    abstract fun taskDao(): TaskDao

    // 5. 'companion object' (объект-компаньон) позволяет нам создать 'синглтон' (Singleton).
    // Это гарантирует, что у нас будет только ОДИН экземпляр базы данных на все приложение,
    // что экономит ресурсы.
    companion object {

        // @Volatile означает, что 'INSTANCE' будет всегда актуален для всех потоков.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Если INSTANCE уже есть, возвращаем его.
            return INSTANCE ?: synchronized(this) {
                // Если нет, 'synchronized' (синхронизированно) создаем его.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taskflow_database" // Имя файла нашей БД на телефоне
                )
                    .fallbackToDestructiveMigration() // Если мы обновим версию, старая БД будет удалена (пока нам это подходит)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}