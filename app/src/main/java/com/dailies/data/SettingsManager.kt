package com.dailies.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dailies_settings", Context.MODE_PRIVATE)

    var highlightCurrentDay: Boolean
        get() = prefs.getBoolean("highlight_current_day", true)
        set(value) = prefs.edit().putBoolean("highlight_current_day", value).apply()

    var showStreakCount: Boolean
        get() = prefs.getBoolean("show_streak_count", true)
        set(value) = prefs.edit().putBoolean("show_streak_count", value).apply()

    var showMonthLabels: Boolean
        get() = prefs.getBoolean("show_month_labels", true)
        set(value) = prefs.edit().putBoolean("show_month_labels", value).apply()
}
