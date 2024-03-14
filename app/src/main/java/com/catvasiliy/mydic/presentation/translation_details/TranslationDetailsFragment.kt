package com.catvasiliy.mydic.presentation.translation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslationDetailsBinding
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import com.catvasiliy.mydic.presentation.util.pronounce.Pronouncer
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.visibleIf
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class TranslationDetailsFragment : Fragment() {

    private var _binding: FragmentTranslationDetailsBinding? = null
    private val binding get() = _binding!!

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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.piTranslation.visibleIf { state.isLoading }
                    val translation = state.translation ?: return@collectLatest
                    setupTranslationLayout(translation)
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
            if (activity.isNavigationDrawerOpen) {
                activity.closeNavigationDrawer()
            } else {
                handleBackNavigation()
            }
        }

        binding.tbTranslation.setNavigationOnClickListener {
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
        val sourceText = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).sourceText
        } ?: ""

        if (sourceText.isBlank()) return false

        val sourceLanguageOrdinal = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).sourceLanguageOrdinal
        } ?: -1

        val sourceLanguage = UiLanguage.entries.getOrNull(sourceLanguageOrdinal)

        val targetLanguage = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).targetLanguage
        }

        arguments?.apply {
            remove("sourceText")
            remove("sourceLanguageOrdinal")
            remove("targetLanguage")
        }

        viewModel.translate(
            sourceText = sourceText,
            sourceLanguage = sourceLanguage,
            targetLanguage = requireNotNull(targetLanguage)
        )
        return true
    }

    private fun handleIdNavArgs(): Boolean {
        val translationId = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).translationId
        } ?: -1L

        if (translationId == -1L) return false

        val isMissingTranslation = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).isMissingTranslation
        } ?: false

        arguments?.apply {
            remove("translationId")
            remove("isMissingTranslation")
        }

        viewModel.loadTranslation(translationId, isMissingTranslation)
        return true
    }

    private fun setupTranslationLayout(translation: UiTranslation) {
        binding.ivPronounceSource.setOnClickListener {
            val sourceText = translation.sourceText
            val languageCode = translation.sourceLanguageCode ?: UiLanguage.ENGLISH.code

            pronounce(sourceText, languageCode)
        }
        if (translation.isMissingTranslation) {
            setupMissingTranslationLayout(translation)
        } else {
            setupExtendedTranslationLayout(translation)
        }
    }

    private fun setupExtendedTranslationLayout(translation: UiTranslation) {
        if (translation.isMissingTranslation)
            throw IllegalArgumentException("Translation is not an ExtendedTranslation")

        val translationText = translation.translationText!!

        binding.llMissingTranslation.hideAndShowOther(binding.clTranslationDetails)

        setupTabLayout()

        binding.tvTranslation.text = translationText

        binding.ivPronounceTranslation.setOnClickListener {
            val languageCode = translation.targetLanguage.code

            pronounce(translationText, languageCode)
        }

        binding.tvSource.text = translation.sourceText
        binding.tvTransliteration.text = translation.sourceTransliteration ?: "No transliteration"
    }

    private fun setupMissingTranslationLayout(missingTranslation: UiTranslation) {
        if (!missingTranslation.isMissingTranslation)
            throw IllegalArgumentException("Translation is not a MissingTranslation")

        binding.clTranslationDetails.hideAndShowOther(binding.llMissingTranslation)

        binding.tvSource.text = missingTranslation.sourceText
        binding.btnRefresh.setOnClickListener {
            viewModel.updateMissingTranslation()
        }
    }

    private fun pronounce(text: String, languageCode: String) {
        val pronouncer = requireActivity() as Pronouncer
        val locale = Locale(languageCode)
        pronouncer.pronounce(text, locale)
    }

    private fun setupTabLayout() {
        val viewPager = binding.viewPager
        viewPager.apply {
            adapter = TranslationDetailsAdapter(this@TranslationDetailsFragment)
            isUserInputEnabled = false
        }

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager, false, false) { tab, position ->
            @StringRes val id = when (position) {
                0 -> R.string.alternatives
                1 -> R.string.definitions
                2 -> R.string.examples
                else -> throw IllegalArgumentException()
            }
            tab.text = getString(id)
        }.attach()
    }
}