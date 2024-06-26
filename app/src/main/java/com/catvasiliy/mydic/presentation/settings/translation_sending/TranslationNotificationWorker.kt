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
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sending.GetTranslationForSendingUseCase
import com.catvasiliy.mydic.domain.util.Resource
import com.catvasiliy.mydic.presentation.model.toUiTranslationForSending
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TranslationNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    @TranslationNotifierQualifier private val notifier: Notifier,
    private val getTranslationForSendingUseCase: GetTranslationForSendingUseCase
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

        val translation = when (val translationResource = getTranslationForSendingUseCase()) {
            is Resource.Success -> translationResource.data!!
            is Resource.Error -> return Result.failure()
        }.toUiTranslationForSending()

        (notifier as TranslationNotifier).apply {
            setTranslationForSending(translation)
        }

        notifier.showNotification()
        return Result.success()
    }

    companion object {
        const val UNIQUE_WORK_NAME = "TRANSLATION_SENDING"
    }
}