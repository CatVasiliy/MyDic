package com.catvasiliy.mydic.presentation.translations_list.list_util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.ItemTranslationBinding
import com.catvasiliy.mydic.presentation.model.translation.UiExtendedLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.translations_list.TranslationsListFragmentDirections
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.showIf

class TranslationsListAdapter
    : ListAdapter<UiTranslationListItem, TranslationViewHolder>(TranslationListItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val binding = ItemTranslationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TranslationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val translationItem = getItem(position)
        holder.bind(translationItem)
    }
}

class TranslationViewHolder(private val binding: ItemTranslationBinding) : ViewHolder(binding.root) {

    fun bind(translationItem: UiTranslationListItem): Unit = with(binding) {

        root.setOnClickListener { view ->
            val action = TranslationsListFragmentDirections.openTranslationDetailsFromList(
                translationId = translationItem.id,
                isMissingTranslation = translationItem.isMissingTranslation
            )
            view.findNavController().navigate(action)
        }

        val sourceLanguage = translationItem.sourceLanguage
        @DrawableRes
        val sourceLanguageDrawable = if (sourceLanguage is UiExtendedLanguage.Known) {
            sourceLanguage.language.drawableResId
        } else {
            R.drawable.language_icon_unknown
        }

        ivSourceLanguage.setImageResource(sourceLanguageDrawable)
        tvSourceText.text = translationItem.sourceText

        if (translationItem.translationText != null) {
            tvMissingTranslationMessage.hideAndShowOther(tvTranslationText)
            tvTranslationText.text = translationItem.translationText
        } else {
            tvTranslationText.hideAndShowOther(tvMissingTranslationMessage)
        }

        ivMissingTranslation.showIf { translationItem.isMissingTranslation }
    }
}

private class TranslationListItemDiff : DiffUtil.ItemCallback<UiTranslationListItem>() {

    override fun areItemsTheSame(
        oldItem: UiTranslationListItem,
        newItem: UiTranslationListItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UiTranslationListItem,
        newItem: UiTranslationListItem
    ): Boolean {
        return oldItem == newItem
    }
}