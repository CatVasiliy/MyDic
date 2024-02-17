package com.catvasiliy.mydic.presentation.settings.translation_sending

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.catvasiliy.mydic.di.TranslationNotifierQualifier
import com.catvasiliy.mydic.domain.use_case.settings.GetTranslationForSendingUseCase
import com.catvasiliy.mydic.domain.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TranslationNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    @TranslationNotifierQualifier private val notifier: Notifier,
    private val getTranslationForSending: GetTranslationForSendingUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.failure()
            }
        }

        val translation = when (val translationResource = getTranslationForSending()) {
            is Resource.Success -> translationResource.data!!
            is Resource.Error -> return Result.failure()
            else -> throw IllegalStateException()
        }

        (notifier as TranslationNotifier).apply {
            setTranslationId(translation.id)
            setSourceText(translation.sourceText)
        }

        notifier.showNotification()
        return Result.success()
    }

    companion object {
        const val UNIQUE_WORK_NAME = "TRANSLATION_SENDING"
    }
}