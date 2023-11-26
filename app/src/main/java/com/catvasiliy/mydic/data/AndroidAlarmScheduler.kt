package com.catvasiliy.mydic.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.catvasiliy.mydic.domain.alarm_scheduler.AlarmScheduler
import java.util.Date

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule() {
//        val intent = Intent(context, AlarmReceiver::class.java).apply {
//            putExtra("EXTRA_MESSAGE", "Test")
//        }
//        alarmManager.set(
//            AlarmManager.RTC,
//            Date().time + 10000L,
//            PendingIntent.getBroadcast(
//                context,
//                123,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        )
    }
}