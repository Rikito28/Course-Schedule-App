package com.dicoding.courseschedule.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.*
import java.util.Calendar

class DailyReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        executeThread {
            val repository = DataRepository.getInstance(context)
            val courses = repository?.getTodaySchedule()

            courses?.let {
                if (it.isNotEmpty()) showNotification(context, it)
            }
        }
    }

    //TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    fun setDailyReminder(context: Context) {
        val reminderAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarm1 = Intent(context, DailyReminder::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        val pendingAlarm = PendingIntent.getBroadcast(context, ID_REPEATING, alarm1, PendingIntent.FLAG_MUTABLE)
        reminderAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingAlarm)
        Toast.makeText(context, "Berhasil Memasang Reminder Alarm", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val reminderAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarm = Intent(context, DailyReminder::class.java)
        val pendingAlarm = PendingIntent.getBroadcast(context, ID_REPEATING, alarm, PendingIntent.FLAG_MUTABLE)
        pendingAlarm.cancel()

        reminderAlarm.cancel(pendingAlarm)
        Toast.makeText(context, "Alarm berhasil diTunda", Toast.LENGTH_SHORT).show()
    }

    private fun showNotification(context: Context, content: List<Course>) {
        //TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = context.resources.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            notificationStyle.addLine(courseData)
        }

        val notif = Intent(context, HomeActivity::class.java)
        val pendingNotification = PendingIntent.getActivity(context, 0, notif, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(pendingNotification)
            .setContentTitle("Kamu Memiliki Jadwal Hari Ini")
            .setSmallIcon(R.drawable.ic_notifications)
            .setStyle(notificationStyle)
            .setAutoCancel(true)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val setNotification = builder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, setNotification)
    }
}