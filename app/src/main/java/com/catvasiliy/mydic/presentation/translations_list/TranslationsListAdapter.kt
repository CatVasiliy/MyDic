package com.catvasiliy.mydic.presentation.translations_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.TranslationListItemBinding
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.util.showIf

class TranslationsListAdapter
    : ListAdapter<UiTranslationListItem, TranslationsListAdapter.TranslationViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val binding = TranslationListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TranslationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val item: UiTranslationListItem = getItem(position)
        holder.bind(item)
    }

    inner class TranslationViewHolder(
        private val binding: TranslationListItemBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: UiTranslationListItem) {

            val isMissingTranslation = item.translationText == null

            binding.root.setOnClickListener { view ->
                val action = TranslationsListFragmentDirections.openTranslationDetailsFromList(
                    translationId = item.id,
                    isMissingTranslation = isMissingTranslation
                )
                view.findNavController().navigate(action)
            }
            binding.tvSource.text = item.sourceText
            binding.tvTranslation.text = item.translationText ?: ""
            binding.ivProblem.showIf { isMissingTranslation }
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UiTranslationListItem>() {

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