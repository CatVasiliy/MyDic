package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.TabFragmentMainTranslationBinding
import com.catvasiliy.mydic.presentation.model.translation.UiExtendedLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsState
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import com.catvasiliy.mydic.presentation.util.pronounce.Pronouncer
import com.catvasiliy.mydic.presentation.util.showIf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainTranslationFragment : Fragment() {

    private var _binding: TabFragmentMainTranslationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFragmentMainTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if (state !is TranslationDetailsState.Translation) return@collectLatest
                    updateView(state.translation)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as Pronouncer).stop()
    }

    private fun updateView(translation: UiTranslation) {
        updateSourceView(translation)
        updateTranslationView(translation)
        updateTransliterationView(translation)
    }

    private fun updateSourceView(translation: UiTranslation) {
        val sourceText = translation.sourceText

        val sourceLanguage = translation.sourceLanguage

        @DrawableRes
        val sourceLanguageDrawableResId = when(sourceLanguage) {
            is UiExtendedLanguage.Known -> sourceLanguage.language.drawableResId
            else -> R.drawable.language_icon_unknown
        }

        val sourceLanguageString = when(sourceLanguage) {
            is UiExtendedLanguage.Known -> getString(sourceLanguage.language.stringResId)
            is UiExtendedLanguage.Unknown -> getString(R.string.language_unknown_with_code, sourceLanguage.languageCode)
            else -> getString(R.string.language_unknown)
        }

        with(binding) {
            tvSourceText.text = sourceText
            ivSourceLanguageIcon.setImageResource(sourceLanguageDrawableResId)
            tvSourceLanguage.text = sourceLanguageString
            btnPronounceSource.setOnClickListener {
                val sourceLanguageCode = translation.sourceLanguageCode ?: UiLanguage.ENGLISH.code

                pronounce(sourceText, sourceLanguageCode)
            }
        }
    }

    private fun updateTranslationView(translation: UiTranslation) {
        val translationText = requireNotNull(translation.translationText)

        val targetLanguage = translation.targetLanguage

        with(binding) {
            tvTranslationText.text = translationText
            ivTargetLanguageIcon.setImageResource(targetLanguage.drawableResId)
            tvTargetLanguage.setText(targetLanguage.stringResId)
            btnPronounceTranslation.setOnClickListener {
                val targetLanguageCode = targetLanguage.code

                pronounce(translationText, targetLanguageCode)
            }
        }
    }

    private fun updateTransliterationView(translation: UiTranslation) {
        val transliteration = translation.sourceTransliteration

        with(binding) {
            dividerTranslation.showIf { transliteration != null }
            tvTransliterationTitle.showIf { transliteration != null }
            tvTransliteration.apply {
                showIf { transliteration != null }
                text = transliteration
            }
        }
    }

    private fun pronounce(text: String, languageCode: String) {
        val pronouncer = requireActivity() as Pronouncer
        val locale = Locale(languageCode)
        pronouncer.pronounce(text, locale)
    }
}