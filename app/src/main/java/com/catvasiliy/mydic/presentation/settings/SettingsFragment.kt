package com.catvasiliy.mydic.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.databinding.FragmentSettingsBinding
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingInterval
import com.catvasiliy.mydic.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private val spinnerItemSelectedListener = object : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as TranslationSendingInterval
            val currentItem = viewModel.state.value.sendTranslationPreferences.sendingInterval
            if (item != currentItem) {
                viewModel.updateTranslationSendingInterval(item)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
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
            checkAndRequestNotificationPermission()
            viewModel.toggleTranslationSending(
                isSendingEnabled = binding.swSendTranslations.isChecked,
                sendingInterval = binding.spSendingIntervals.selectedItem as TranslationSendingInterval
            )
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            TranslationSendingInterval.entries
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spSendingIntervals.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = spinnerItemSelectedListener
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val isSendingEnabled = state.sendTranslationPreferences.isSendingEnabled
                    binding.swSendTranslations.isChecked = isSendingEnabled

                    val selection = state.sendTranslationPreferences.sendingInterval
                    val position = spinnerAdapter.getPosition(selection)
                    binding.spSendingIntervals.apply {
                        // Disable intervals spinner if translation sending enabled
                        isEnabled = !isSendingEnabled
                        setSelection(position)
                    }
                }
            }
        }
        startPostponedEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}