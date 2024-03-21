package com.catvasiliy.mydic.presentation.translations_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.ItemTranslationBinding
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
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

        tvSource.text = translationItem.sourceText
        tvTranslation.text = translationItem.translationText ?: ""
        ivProblem.showIf { translationItem.isMissingTranslation }
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