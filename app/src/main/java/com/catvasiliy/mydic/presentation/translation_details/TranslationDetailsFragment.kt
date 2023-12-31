package com.catvasiliy.mydic.presentation.translation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslationDetailsBinding
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.presentation.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        handleNavigationArguments()

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                showProgressBar(state.isLoading)
                val translation = state.translation ?: return@collectLatest
                when (translation) {
                    is ExtendedTranslation -> createTranslationView(translation)
                    is MissingTranslation -> createMissingTranslationView(translation)
                }
            }
        }
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

    private fun handleNavigationArguments() {
        val sourceText = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).sourceText
        } ?: ""

        if (sourceText.isNotBlank()) {
            viewModel.translate(sourceText)
            return
        }

        val id = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).translationId
        } ?: -1L

        if (id == -1L) {
            return
        }

        val isMissingTranslation = arguments?.let { args ->
            TranslationDetailsFragmentArgs.fromBundle(args).isMissingTranslation
        } ?: false

        arguments?.remove("translationId")
        arguments?.remove("isMissingTranslation")

        viewModel.loadTranslation(id, isMissingTranslation)
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.piTranslation.visibility = VISIBLE
        } else {
            binding.piTranslation.visibility = INVISIBLE
        }
    }

    private fun createTranslationView(translation: ExtendedTranslation) {
        binding.llMissingTranslation.visibility = GONE
        binding.llTranslationDetails.visibility = VISIBLE

        setupTabLayout()
        binding.tvTranslation.text = translation.translationText
        binding.tvSource.text = translation.sourceText
        binding.tvTransliteration.text = translation.sourceTransliteration
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

    private fun createMissingTranslationView(missingTranslation: MissingTranslation) {
        binding.llTranslationDetails.visibility = GONE
        binding.llMissingTranslation.visibility = VISIBLE

        binding.tvSource.text = missingTranslation.sourceText
        binding.btnRefresh.setOnClickListener {
            viewModel.updateMissingTranslation()
        }
    }
}