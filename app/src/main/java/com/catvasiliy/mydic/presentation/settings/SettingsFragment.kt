package com.catvasiliy.mydic.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.databinding.FragmentSettingsBinding
import com.catvasiliy.mydic.domain.model.settings.Period
import com.catvasiliy.mydic.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View?,
            position: Int,
            id: Long
        ) {
            val item = parent.getItemAtPosition(position) as Period
            viewModel.setPeriod(item)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbSettings.setNavigationOnClickListener {
            (requireActivity() as MainActivity).openNavigationDrawer()
        }

        binding.swSendTranslations.setOnClickListener {
            viewModel.setIsSendingEnabled(binding.swSendTranslations.isChecked)
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Period.entries
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spPeriods.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = spinnerItemSelectedListener
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.swSendTranslations.isChecked =
                        state.sendTranslationPreferences.isSendingEnabled

                    val selection = state.sendTranslationPreferences.period
                    val position = spinnerAdapter.getPosition(selection)
                    binding.spPeriods.setSelection(position)
                }
            }
        }
        startPostponedEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}