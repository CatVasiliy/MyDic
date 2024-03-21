package com.catvasiliy.mydic.presentation.translate.spinners

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
            R.layout.item_language_spinner,
            parent,
            false
        )
        val item = items[position]

        @DrawableRes
        val drawableResId = item.language?.drawableResId ?: R.drawable.language_icon_auto

        @StringRes
        val stringResId = item.language?.stringResId ?: R.string.language_auto

        view.findViewById<ImageView>(R.id.ivLanguageItemIcon).setImageResource(drawableResId)
        view.findViewById<TextView>(R.id.tvLanguageItemText).setText(stringResId)

        return view
    }

    fun getPosition(sourceLanguage: UiLanguage?): Int {
        return items.indexOfFirst { sourceLanguageItem ->
            sourceLanguageItem.language == sourceLanguage
        }
    }

    private fun createItemListFromUiLanguageEntries(): List<SourceLanguageSpinnerItem> {
        return UiLanguage.entries.map { language ->
            SourceLanguageSpinnerItem(language)
        }
    }
}