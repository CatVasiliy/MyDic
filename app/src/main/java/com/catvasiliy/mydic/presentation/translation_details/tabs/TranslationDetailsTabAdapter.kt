package com.catvasiliy.mydic.presentation.translation_details.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catvasiliy.mydic.presentation.translation_details.tabs.alternative_translations.AlternativeTranslationsFragment
import com.catvasiliy.mydic.presentation.translation_details.tabs.definitions.DefinitionsFragment
import com.catvasiliy.mydic.presentation.translation_details.tabs.examples.ExamplesFragment

class TranslationDetailsTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MainTranslationFragment()
            1 -> AlternativeTranslationsFragment()
            2 -> DefinitionsFragment()
            3 -> ExamplesFragment()
            else -> throw IllegalArgumentException()
        }
    }
}