package com.catvasiliy.mydic.presentation.model.preferences.translation_sending

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R

enum class UiTranslationSendingInterval(@StringRes val stringResId: Int) {
    MINUTES_1(R.string.interval_minutes_1),
    MINUTES_2(R.string.interval_minutes_2),
    HOURS_3(R.string.interval_hours_3),
    HOURS_6(R.string.interval_hours_6),
    HOURS_12(R.string.interval_hours_12);
}
