package com.catvasiliy.mydic.presentation.translations_list.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.UiSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

class SourceLanguageFilterSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private val items = buildList {
        add(SourceLanguageFilterSpinnerItem.LanguageAny)
        add(SourceLanguageFilterSpinnerItem.LanguageUnknown)
        addAll(createLanguageKnownItemList())
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

        view.findViewById<TextView>(R.id.tvLanguageItemText).text = context.getString(item.stringResourceId)

        return view
    }

    fun getPosition(filteringInfo: UiSourceLanguageFilteringInfo): Int {
        val spinnerItem = when (filteringInfo) {
            is UiSourceLanguageFilteringInfo.LanguageAny ->
                SourceLanguageFilterSpinnerItem.LanguageAny
            is UiSourceLanguageFilteringInfo.LanguageUnknown ->
                SourceLanguageFilterSpinnerItem.LanguageUnknown
            is UiSourceLanguageFilteringInfo.LanguageKnown ->
                SourceLanguageFilterSpinnerItem.LanguageKnown(filteringInfo.language)
        }

        return items.indexOfFirst { filteringSpinnerItem ->
            filteringSpinnerItem == spinnerItem
        }
    }

    private fun createLanguageKnownItemList(): List<SourceLanguageFilterSpinnerItem.LanguageKnown> {
        return UiLanguage.entries.map { entry ->
            SourceLanguageFilterSpinnerItem.LanguageKnown(entry)
        }
    }
}