package com.example.wellnessmate.repository

import android.content.Context
import androidx.work.*
import com.example.wellnessmate.worker.HydrationReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

interface NotificationRepository {
    suspend fun scheduleHydrationReminders(startHour: Int, endHour: Int, intervalHours: Int)
    suspend fun cancelHydrationReminders()
    suspend fun cancelAllNotifications()
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationRepository {

    companion object {
        private const val HYDRATION_REMINDER_WORK_NAME = "hydration_reminder_work"
        private const val HYDRATION_REMINDER_TAG = "hydration_reminder"
    }

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleHydrationReminders(
        startHour: Int,
        endHour: Int,
        intervalHours: Int
    ) {
        // Cancel existing reminders first
        cancelHydrationReminders()

        // Create constraints for the work
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false)
            .build()

        // Create input data for the worker
        val inputData = Data.Builder()
            .putInt("start_hour", startHour)
            .putInt("end_hour", endHour)
            .putInt("interval_hours", intervalHours)
            .build()

        // Create periodic work request
        val hydrationReminderWork = PeriodicWorkRequestBuilder<HydrationReminderWorker>(
            intervalHours.toLong(), TimeUnit.HOURS,
            15, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(HYDRATION_REMINDER_TAG)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        // Enqueue the work with unique name
        workManager.enqueueUniquePeriodicWork(
            HYDRATION_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            hydrationReminderWork
        )
    }

    override suspend fun cancelHydrationReminders() {
        // Cancel work by unique name
        workManager.cancelUniqueWork(HYDRATION_REMINDER_WORK_NAME)

        // Also cancel by tag as backup
        workManager.cancelAllWorkByTag(HYDRATION_REMINDER_TAG)
    }

    override suspend fun cancelAllNotifications() {
        // Cancel all work
        workManager.cancelAllWork()

        // Also cancel system notifications if needed
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as android.app.NotificationManager
        notificationManager.cancelAll()
    }
}