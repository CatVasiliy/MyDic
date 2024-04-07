package com.catvasiliy.mydic.presentation.translation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslationDetailsBinding
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.model.translation.UiExtendedLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.translation_details.tabs.TranslationDetailsTabAdapter
import com.catvasiliy.mydic.presentation.util.hide
import com.catvasiliy.mydic.presentation.util.pronounce.Pronouncer
import com.catvasiliy.mydic.presentation.util.show
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class TranslationDetailsFragment : Fragment() {

    private var _binding: FragmentTranslationDetailsBinding? = null
    private val binding get() = _binding!!

    private val navArgs: TranslationDetailsFragmentArgs by navArgs()

    private val viewModel: TranslationDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackAction()
        handleNavArgs()
        setupTabLayout()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is TranslationDetailsState.Loading -> showLoadingView()
                        is TranslationDetailsState.Translation -> updateTranslationView()
                        is TranslationDetailsState.MissingTranslation -> updateMissingTranslationView(state.missingTranslation)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as Pronouncer).stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBackAction() {
        val activity = requireActivity() as MainActivity

        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackNavigation()
        }

        binding.tbTranslationDetails.setNavigationOnClickListener {
            handleBackNavigation()
        }
    }

    private fun handleBackNavigation() {
        findNavController().popBackStack(R.id.fragmentTranslationsList, false)
    }

    private fun handleNavArgs() {
        if (handleSourceTextNavArgs()) return
        if (handleIdNavArgs()) return
    }

    private fun handleSourceTextNavArgs(): Boolean {
        val sourceText = navArgs.sourceText

        if (sourceText.isBlank()) return false

        val sourceLanguageOrdinal = navArgs.sourceLanguageOrdinal
        val sourceLanguage = UiLanguage.entries.getOrNull(sourceLanguageOrdinal)

        val targetLanguage = navArgs.targetLanguage

        arguments?.apply {
            remove("sourceText")
            remove("sourceLanguageOrdinal")
            remove("targetLanguage")
        }

        viewModel.translate(
            sourceText = sourceText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
        return true
    }

    private fun handleIdNavArgs(): Boolean {
        val translationId = navArgs.translationId

        if (translationId == -1L) return false

        val isMissingTranslation = navArgs.isMissingTranslation

        arguments?.apply {
            remove("translationId")
            remove("isMissingTranslation")
        }

        viewModel.loadTranslation(translationId, isMissingTranslation)
        return true
    }

    private fun setupTabLayout() {
        val viewPagerAdapter = TranslationDetailsTabAdapter(this)

        val viewPager = binding.viewPager
        viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = viewPagerAdapter.itemCount - 1
        }

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager, false) { tab, position ->
            @StringRes val stringResId = when (position) {
                0 -> R.string.main_translation
                1 -> R.string.alternatives
                2 -> R.string.definitions
                3 -> R.string.examples
                else -> throw IllegalArgumentException()
            }
            tab.setText(stringResId)
        }.attach()
    }

    private fun updateTranslationView() {
        showTranslationView()
    }

    private fun updateMissingTranslationView(missingTranslation: UiTranslation) {
        val sourceText = missingTranslation.sourceText
        val sourceLanguage = missingTranslation.sourceLanguage
        val targetLanguage = missingTranslation.targetLanguage

        @DrawableRes
        val sourceLanguageDrawableResId = when(sourceLanguage) {
            is UiExtendedLanguage.Known -> sourceLanguage.language.drawableResId
            else -> R.drawable.language_icon_unknown
        }

        @StringRes
        val sourceLanguageStringResId = when(sourceLanguage) {
            is UiExtendedLanguage.Known -> sourceLanguage.language.drawableResId
            else -> R.string.language_unknown
        }

        with(binding) {
            tvMissingTranslationSource.text = sourceText
            ivMissingTranslationSourceLanguageIcon.setImageResource(sourceLanguageDrawableResId)
            tvMissingTranslationSourceLanguage.setText(sourceLanguageStringResId)
            ivMissingTranslationTargetLanguageIcon.setImageResource(targetLanguage.drawableResId)
            tvMissingTranslationTargetLanguage.setText(targetLanguage.stringResId)
            btnPronounceMissingSource.setOnClickListener {
                val sourceLanguageCode = missingTranslation.sourceLanguageCode ?: UiLanguage.ENGLISH.code

                pronounce(sourceText, sourceLanguageCode)
            }
            btnTranslateAgain.setOnClickListener {
                viewModel.updateMissingTranslation()
            }
        }

        showMissingTranslationView()
    }

    private fun showLoadingView() = with(binding) {
        clTranslationDetails.hide()
        svMissingTranslation.hide()
        piTranslationDetails.visibility = View.VISIBLE
    }

    private fun showTranslationView() = with(binding) {
        piTranslationDetails.visibility = View.GONE
        svMissingTranslation.hide()
        clTranslationDetails.show()
    }

    private fun showMissingTranslationView() = with(binding) {
        piTranslationDetails.visibility = View.GONE
        clTranslationDetails.hide()
        svMissingTranslation.show()
    }

    private fun pronounce(text: String, languageCode: String) {
        val pronouncer = requireActivity() as Pronouncer
        val locale = Locale(languageCode)
        pronouncer.pronounce(text, locale)
    }
}