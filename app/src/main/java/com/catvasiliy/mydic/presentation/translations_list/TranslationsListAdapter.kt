package com.catvasiliy.mydic.presentation.translations_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.TranslationListItemBinding
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.SimpleTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.presentation.util.showIf

class TranslationsListAdapter
    : ListAdapter<Translation, TranslationsListAdapter.TranslationViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val binding = TranslationListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TranslationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val item: Translation = getItem(position)
        holder.bind(item)
    }

    inner class TranslationViewHolder(
        private val binding: TranslationListItemBinding
    ) : ViewHolder(binding.root) {

        fun bind(item: Translation) {
            binding.root.setOnClickListener { view ->
                val action = TranslationsListFragmentDirections.openTranslationDetailsFromList(
                    translationId = item.id,
                    isMissingTranslation = item is MissingTranslation
                )
                view.findNavController().navigate(action)
            }
            binding.tvSource.text = item.sourceText
            binding.tvTranslation.text = if (item is SimpleTranslation) item.translationText else ""
            binding.ivProblem.showIf { item is MissingTranslation }
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Translation>() {

    override fun areItemsTheSame(oldItem: Translation, newItem: Translation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Translation,
        newItem: Translation
    ): Boolean {
        return oldItem == newItem
    }
}