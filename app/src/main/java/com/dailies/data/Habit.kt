package com.dailies.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val targetCompletions: Int = 1,
    val currentCompletions: Int = 0,
    val streak: Int = 0,
    val totalCompletions: Int = 0,
    val lastCompletedDate: Long = 0L,
    val colorSeed: Long = 0xFF6200EE
)
