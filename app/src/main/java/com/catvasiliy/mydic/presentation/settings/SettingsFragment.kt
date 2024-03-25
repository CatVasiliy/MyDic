package com.catvasiliy.mydic.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingInterval
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingPreferences
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val sendTranslationsKey = "sendTranslations"
private const val sendingIntervalKey = "sendingInterval"

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    private val sendTranslationsListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        if (preference !is SwitchPreferenceCompat) return@OnPreferenceChangeListener false

        val isChecked = newValue as Boolean
        if (isChecked) {
            checkAndRequestNotificationPermission()
        }

        val swSendingInterval = findPreference<ListPreference>(sendingIntervalKey)
            ?: return@OnPreferenceChangeListener false
        val sendingIntervalName = swSendingInterval.value ?: return@OnPreferenceChangeListener false
        val sendingInterval = UiTranslationSendingInterval.valueOf(sendingIntervalName)

        viewModel.toggleTranslationSending(isChecked, sendingInterval)

        true
    }

    private val sendingIntervalListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        if (preference !is ListPreference) return@OnPreferenceChangeListener false

        val sendingIntervalName = newValue as String
        val sendingInterval = UiTranslationSendingInterval.valueOf(sendingIntervalName)
        viewModel.updateTranslationSendingInterval(sendingInterval)

        true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    updateSendingPreferencesView(state.sendingPreferences)
                }
            }
        }
    }

    private fun setupView() {
        view?.findViewById<MaterialToolbar>(R.id.tbSettings)?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        findPreference<SwitchPreferenceCompat>(sendTranslationsKey)?.apply {
            onPreferenceChangeListener = sendTranslationsListener
        }

        findPreference<ListPreference>(sendingIntervalKey)?.apply {
            entries = getSendingIntervalEntries()
            entryValues = getSendingIntervalEntryValues()
            onPreferenceChangeListener = sendingIntervalListener
        }
    }

    private fun getSendingIntervalEntries(): Array<CharSequence> {
        return UiTranslationSendingInterval.entries.map { entry ->
            getString(entry.stringResId)
        }.toTypedArray()
    }

    private fun getSendingIntervalEntryValues(): Array<CharSequence> {
        return UiTranslationSendingInterval.entries.map { entry ->
            entry.name
        }.toTypedArray()
    }

    private fun updateSendingPreferencesView(sendingPreferences: UiTranslationSendingPreferences) {
        val isSendingEnabled = sendingPreferences.isSendingEnabled

        findPreference<SwitchPreferenceCompat>(sendTranslationsKey)?.isChecked = isSendingEnabled

        findPreference<ListPreference>(sendingIntervalKey)?.apply {
            // Disable intervals if translation sending enabled
            isEnabled = !isSendingEnabled
            value = sendingPreferences.sendingInterval.name
        }
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