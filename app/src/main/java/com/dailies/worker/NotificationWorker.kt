package com.dailies.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dailies.data.AppDatabase
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(context)
        val habits = database.habitDao().getAllHabits().first()

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val uncompleted = habits.filter { it.lastCompletedDate < today || it.currentCompletions < it.targetCompletions }

        if (uncompleted.isNotEmpty()) {
            showNotification(uncompleted.size)
        }

        return Result.success()
    }

    private fun showNotification(uncompletedCount: Int) {
        val channelId = "dailies_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Time for your Dailies!")
            .setContentText("You have $uncompletedCount habits left to complete today. Keep up the streak!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }

    companion object {
        fun scheduleDailyReminder(context: Context) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()

            dueDate.set(Calendar.HOUR_OF_DAY, 8)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

            val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "daily_reminder",
                    ExistingPeriodicWorkPolicy.KEEP,
                    dailyWorkRequest
                )
        }
    }
}
