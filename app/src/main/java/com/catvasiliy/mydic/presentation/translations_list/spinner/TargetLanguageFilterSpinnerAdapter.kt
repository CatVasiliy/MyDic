package com.catvasiliy.mydic.presentation.translations_list.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

class TargetLanguageFilterSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private val items = buildList {
        add(TargetLanguageFilterSpinnerItem.LanguageAny)
        addAll(createLanguageKnownItemList())
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_language_spinner,
            parent,
            false
        )
        val item = items[position]

        view.findViewById<ImageView>(R.id.ivLanguageItemIcon).setImageResource(item.drawableResId)
        view.findViewById<TextView>(R.id.tvLanguageItemText).setText(item.stringResId)

        return view
    }

    fun getPosition(filteringInfo: UiTargetLanguageFilteringInfo): Int {
        return items.indexOfFirst { filterSpinnerItem ->
            filterSpinnerItem.filteringInfo == filteringInfo
        }
    }

    private fun createLanguageKnownItemList(): List<TargetLanguageFilterSpinnerItem> {
        return UiLanguage.entries.map { language ->
            val filteringInfo = UiTargetLanguageFilteringInfo.LanguageKnown(language)
            TargetLanguageFilterSpinnerItem.LanguageKnown(filteringInfo)
        }
    }
}