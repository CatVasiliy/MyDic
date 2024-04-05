package com.catvasiliy.mydic.presentation.translation_details.tabs.definitions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.databinding.ItemDefinitionBinding
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.presentation.util.showIf

class DefinitionsAdapter(
    private val definitions: List<Definition>
) : RecyclerView.Adapter<DefinitionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        val binding = ItemDefinitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DefinitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        val definitionItem = definitions[position]
        holder.bind(definitionItem)
    }

    override fun getItemCount(): Int = definitions.size
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