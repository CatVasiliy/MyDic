package com.catvasiliy.mydic.presentation.settings.translation_sending

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.util.ACTION_OPEN_TRANSLATION
import com.catvasiliy.mydic.presentation.util.EXTRA_TRANSLATION_ID
import javax.inject.Inject

class TranslationNotifier @Inject constructor(
    private val context: Context,
    notificationManager: NotificationManager
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "TRANSLATION_CHANNEL"
    override val notificationChannelName: String = "Translation"
    override val notificationId: Int
        get() = translationId.toInt()

    private var translationId: Long = -1
    private var sourceText: String = ""

    override fun buildNotification(): Notification {
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(sourceText)
            .setContentIntent(getTranslationPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    fun setTranslationId(id: Long) {
        this.translationId = id
    }

    fun setSourceText(sourceText: String) {
        this.sourceText = sourceText
    }

    private fun getTranslationPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_OPEN_TRANSLATION
        }
        .putExtra(EXTRA_TRANSLATION_ID, translationId)

        return PendingIntent.getActivity(
            context,
            translationId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}