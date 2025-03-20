package com.example.eventexam.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eventexam.R
import com.example.eventexam.data.EventRepository
import com.example.eventexam.data.database.EventDatabase
import com.example.eventexam.data.remote.response.ListEventsItem
import com.example.eventexam.data.remote.retorfit.ApiConfig
import com.example.eventexam.ui.DetailEventActivity

class DailyReminderWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val repository = EventRepository(ApiConfig.getApiService(), EventDatabase.getDatabase(applicationContext).eventDao())
        val event = repository.getNearestEvent()

        if (event != null) {
            sendNotification(event)
        }
        return Result.success()
    }

    private fun sendNotification(event: ListEventsItem) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_reminder_channel"

        val channel = NotificationChannel(
            channelId, "Daily Reminder", NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, DetailEventActivity::class.java).apply {
            putExtra("EXTRA_DATA", event)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(event.name)
            .setContentText("Event akan dimulai pada ${event.beginTime}")
            .setSmallIcon(R.drawable.img)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}
