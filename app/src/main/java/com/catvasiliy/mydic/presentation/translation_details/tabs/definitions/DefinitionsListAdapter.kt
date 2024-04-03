package com.catvasiliy.mydic.presentation.translation_details.tabs.definitions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.ItemDefinitionBinding
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.presentation.util.showIf

class DefinitionsListAdapter : ListAdapter<Definition, DefinitionViewHolder>(DefinitionItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        val binding = ItemDefinitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DefinitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        val definitionItem = getItem(position)
        holder.bind(definitionItem)
    }
}

class DefinitionViewHolder(
    private val binding: ItemDefinitionBinding
) : ViewHolder(binding.root) {

    fun bind(definition: Definition): Unit = with(binding) {
        tvDefinitionPartOfSpeech.text = definition.partOfSpeech
        tvDefinition.text = definition.definitionText

        val exampleText = definition.exampleText
        dividerNoExample.showIf { exampleText == null }
        tvDefinitionExampleTitle.showIf { exampleText != null }
        tvDefinitionExample.apply {
            showIf { exampleText != null }
            text = exampleText
        }
        dividerDefinition.showIf { exampleText != null }
    }
}

private class DefinitionItemDiff : DiffUtil.ItemCallback<Definition>() {

    override fun areItemsTheSame(oldItem: Definition, newItem: Definition): Boolean {
        return oldItem.definitionText == newItem.definitionText
    }

    override fun areContentsTheSame(oldItem: Definition, newItem: Definition): Boolean {
        return oldItem == newItem
    }
}