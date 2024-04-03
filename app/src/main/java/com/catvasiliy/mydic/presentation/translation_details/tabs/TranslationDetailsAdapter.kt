package com.catvasiliy.mydic.presentation.translation_details.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TranslationDetailsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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