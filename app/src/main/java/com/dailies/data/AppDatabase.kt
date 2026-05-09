package com.dailies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habit_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Add onboarding default habit
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.habitDao()?.insert(
                                Habit(
                                    name = "Drink Water",
                                    description = "Stay hydrated!",
                                    targetCompletions = 3,
                                    colorSeed = 0xFF03A9F4
                                )
                            )
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
