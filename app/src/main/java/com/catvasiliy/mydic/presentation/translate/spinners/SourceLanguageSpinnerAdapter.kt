package com.catvasiliy.mydic.presentation.translate.spinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

class SourceLanguageSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private val items: List<SourceLanguageSpinnerItem> = buildList {
        add(SourceLanguageSpinnerItem(null))
        addAll(createItemListFromUiLanguageEntries())
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.language_spinner_item,
            parent,
            false
        )
        val item = items[position]
        val itemText = if (item.language != null) {
            context.getString(item.language.stringResourceId)
        } else {
            context.getString(R.string.language_auto)
        }

        view.findViewById<TextView>(R.id.tvLanguageItemText).text = itemText

        return view
    }

    fun getPosition(sourceLanguage: UiLanguage?): Int {
        return items.indexOfFirst { sourceLanguageItem ->
            sourceLanguageItem.language == sourceLanguage
        }
    }

    private fun createItemListFromUiLanguageEntries(): List<SourceLanguageSpinnerItem> {
        return UiLanguage.entries.map { entry ->
            SourceLanguageSpinnerItem(entry)
        }
    }
}