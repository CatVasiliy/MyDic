package com.catvasiliy.mydic.presentation.settings.translation_sending

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class TranslationSendingAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val workRequest = OneTimeWorkRequestBuilder<TranslationNotificationWorker>().build()
        val workManager = WorkManager.getInstance(context)

        workManager.beginUniqueWork(
            TranslationNotificationWorker.UNIQUE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
        .enqueue()
    }
}