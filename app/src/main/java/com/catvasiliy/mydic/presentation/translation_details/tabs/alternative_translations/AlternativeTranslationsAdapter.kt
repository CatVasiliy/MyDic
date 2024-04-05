package com.catvasiliy.mydic.presentation.translation_details.tabs.alternative_translations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.ItemAlternativeTranslationBinding
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation

class AlternativeTranslationsAdapter(
    private val alternativeTranslations: List<AlternativeTranslation>
) : RecyclerView.Adapter<AlternativeTranslationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlternativeTranslationViewHolder {
        val binding = ItemAlternativeTranslationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlternativeTranslationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlternativeTranslationViewHolder, position: Int) {
        val alternativeTranslationItem = alternativeTranslations[position]
        holder.bind(alternativeTranslationItem)
    }

    override fun getItemCount(): Int = alternativeTranslations.size
}

class AlternativeTranslationViewHolder(
    private val binding: ItemAlternativeTranslationBinding
) : ViewHolder(binding.root) {

    fun bind(alternativeTranslation: AlternativeTranslation): Unit = with(binding) {
        tvAlternativePartOfSpeech.text = alternativeTranslation.partOfSpeech
        tvAlternativeTranslation.text = alternativeTranslation.translationText
        tvAlternativeSynonyms.text = alternativeTranslation.synonyms
            .joinToString(separator = ", ")
    }
}