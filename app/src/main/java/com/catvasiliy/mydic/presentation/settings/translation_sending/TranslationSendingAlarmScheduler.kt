package com.catvasiliy.mydic.presentation.settings.translation_sending

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingInterval
import javax.inject.Inject

class TranslationSendingAlarmScheduler @Inject constructor(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    private val requestCode: Int = 100

    fun schedule(interval: TranslationSendingInterval) {
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            interval.getFromNowMillis(),
            interval.millis,
            getPendingIntent()
        )
    }

    fun cancel() {
        alarmManager.cancel(getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, TranslationSendingAlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}