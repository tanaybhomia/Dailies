package com.dailies

import android.app.Application
import com.dailies.data.AppDatabase
import com.dailies.data.HabitRepository

class DailiesApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { HabitRepository(database.habitDao()) }
}
