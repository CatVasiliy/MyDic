package com.catvasiliy.mydic.presentation.translation_details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catvasiliy.mydic.presentation.translation_details.tabs.AlternativeTranslationsFragment
import com.catvasiliy.mydic.presentation.translation_details.tabs.DefinitionsFragment
import com.catvasiliy.mydic.presentation.translation_details.tabs.ExamplesFragment

class TranslationDetailsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AlternativeTranslationsFragment()
            1 -> DefinitionsFragment()
            2 -> ExamplesFragment()
            else -> throw IllegalArgumentException()
        }
    }
}