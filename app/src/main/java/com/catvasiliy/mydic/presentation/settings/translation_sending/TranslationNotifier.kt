package com.catvasiliy.mydic.presentation.settings.translation_sending

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationForSending
import com.catvasiliy.mydic.presentation.util.ACTION_OPEN_TRANSLATION
import com.catvasiliy.mydic.presentation.util.EXTRA_TRANSLATION_ID
import javax.inject.Inject

class TranslationNotifier @Inject constructor(
    private val context: Context,
    notificationManager: NotificationManager
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "TRANSLATION_CHANNEL"
    override val notificationChannelName: String = "Translation sending"
    override val notificationId: Int
        get() = translation.id.toInt()

    private var _translation: UiTranslationForSending? = null
    private val translation get() = _translation!!

    override fun buildNotification(): Notification {
        @DrawableRes
        val largeIconResId = translation.sourceLanguage?.drawableResId
            ?: R.drawable.language_icon_unknown

        val largeIcon = ContextCompat.getDrawable(context, largeIconResId)?.toBitmap()

        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.notification_icon_translate_small)
            .setContentTitle(context.getString(R.string.translation_notification_title))
            .setContentText(translation.sourceText)
            .setLargeIcon(largeIcon)
            .setContentIntent(getTranslationPendingIntent())
            .setAutoCancel(true)
            .build()
    }

    fun setTranslationForSending(translationForSending: UiTranslationForSending) {
        _translation = translationForSending
    }

    private fun getTranslationPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_OPEN_TRANSLATION
        }
        .putExtra(EXTRA_TRANSLATION_ID, translation.id)

        return PendingIntent.getActivity(
            context,
            translation.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}