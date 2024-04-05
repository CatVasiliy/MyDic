package com.catvasiliy.mydic.presentation.translation_details.tabs.examples

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.ItemExampleBinding
import com.catvasiliy.mydic.domain.model.translation.Example

class ExamplesAdapter(
    private val examples: List<Example>
) : RecyclerView.Adapter<ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val binding = ItemExampleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExampleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val exampleItem = examples[position]
        holder.bind(exampleItem, position)
    }

    override fun getItemCount(): Int = examples.size
}

class ExampleViewHolder(
    private val binding: ItemExampleBinding
) : ViewHolder(binding.root) {

    fun bind(example: Example, position: Int): Unit = with(binding) {
        val exampleTitleText = itemView.context.getString(R.string.example_with_number, position + 1)
        tvExampleTitle.text = exampleTitleText
        tvExample.text = HtmlCompat.fromHtml(
            example.exampleText,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }
}