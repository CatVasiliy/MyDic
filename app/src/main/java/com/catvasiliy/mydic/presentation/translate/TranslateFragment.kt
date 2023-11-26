package com.catvasiliy.mydic.presentation.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslateBinding
import com.google.android.material.snackbar.Snackbar

class TranslateFragment : Fragment() {

    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbTranslation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.getString("sourceText")?.let { sourceText ->
            binding.etSource.setText(sourceText)
        }
        arguments?.remove("sourceText")

        binding.btnTranslate.setOnClickListener {
            val sourceText = binding.etSource.text.toString()
            if (sourceText.isBlank()) {
                Snackbar.make(
                    binding.root,
                    R.string.empty_source_snackbar,
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val action = TranslateFragmentDirections.openTranslationDetailsFromTranslate(
                binding.etSource.text.toString()
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}