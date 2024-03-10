package com.catvasiliy.mydic.presentation.translate.spinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

class TargetLanguageSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private val items: List<TargetLanguageSpinnerItem> = createItemListFromUiLanguageEntries()

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

        view.findViewById<TextView>(R.id.tvLanguageItemText).text = context.getString(item.language.stringResourceId)

        return view
    }

    fun getPosition(targetLanguage: UiLanguage): Int {
        return items.indexOfFirst { targetLanguageItem ->
            targetLanguageItem.language == targetLanguage
        }
    }

    private fun createItemListFromUiLanguageEntries(): List<TargetLanguageSpinnerItem> {
        return UiLanguage.entries.map { entry ->
            TargetLanguageSpinnerItem(entry)
        }
    }
}