package com.catvasiliy.mydic.presentation.translations_list.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.catvasiliy.mydic.R

sealed interface TranslationsListVisibility {

    data object Visible : TranslationsListVisibility

    enum class Gone(
        @DrawableRes val drawableResId: Int,
        @StringRes val stringResId: Int
    ) : TranslationsListVisibility {

        SAVED_NOTHING(R.drawable.no_translations_saved, R.string.no_translations_saved),
        FILTER_NOTHING(R.drawable.no_translations_filter, R.string.no_translations_filter),
        SEARCH_NOTHING(R.drawable.no_translations_search, R.string.no_translations_search)
    }
}