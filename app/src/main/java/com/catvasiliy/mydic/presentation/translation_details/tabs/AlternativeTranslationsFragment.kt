package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.databinding.AlternativeTranslationItemBinding
import com.catvasiliy.mydic.databinding.FragmentAlternativeTranslationsBinding
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlternativeTranslationsFragment : Fragment() {

    private var _binding: FragmentAlternativeTranslationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlternativeTranslationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val alternativeTranslations = if (state.translation is ExtendedTranslation) {
                        state.translation.alternativeTranslations
                    } else {
                        emptyList()
                    }
                    createAlternativeTranslationsViews(alternativeTranslations)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createAlternativeTranslationsViews(
        alternativeTranslations: List<AlternativeTranslation>
    ) {
        if (alternativeTranslations.isEmpty()) {
            binding.svAlternativeTranslations.hideAndShowOther(binding.llNoAlternativeTranslations)
            return
        }

        binding.svAlternativeTranslations.show()

        alternativeTranslations.forEach { alternativeTranslation ->
            val alternativeTranslationBinding = AlternativeTranslationItemBinding
                .inflate(layoutInflater)
            alternativeTranslationBinding.apply {
                tvAlternativePartOfSpeech.text = alternativeTranslation.partOfSpeech
                tvAlternativeTranslation.text = alternativeTranslation.translationText
                tvAlternativeSynonyms.text = alternativeTranslation.synonyms
                    .joinToString(separator = ", ")
            }
            binding.llAlternativeTranslations.addView(alternativeTranslationBinding.root)
        }
    }
}