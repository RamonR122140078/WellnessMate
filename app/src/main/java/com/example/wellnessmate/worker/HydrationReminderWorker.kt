package com.example.wellnessmate.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wellnessmate.R.drawable.ic_water_drop

class HydrationReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            createNotification()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createNotification() {
        val context = applicationContext
        val channelId = "hydration_reminder"

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Hydration Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders to drink water"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(ic_water_drop)
            .setContentTitle("Time to Drink Water!")
            .setContentText("Stay hydrated! Don't forget to log your water intake.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // Show notification
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(System.currentTimeMillis().toInt(), notification)
            }
        }
    }
}