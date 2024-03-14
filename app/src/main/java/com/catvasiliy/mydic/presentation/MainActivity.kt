package com.catvasiliy.mydic.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.ActivityMainBinding
import com.catvasiliy.mydic.presentation.translate.KEY_EXTERNAL_SOURCE_TEXT
import com.catvasiliy.mydic.presentation.util.ACTION_OPEN_TRANSLATION
import com.catvasiliy.mydic.presentation.util.EXTRA_TRANSLATION_ID
import com.catvasiliy.mydic.presentation.util.pronounce.Pronouncer
import com.catvasiliy.mydic.presentation.util.pronounce.PronunciationSynthesizer
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Pronouncer {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val drawerLayout = binding.drawerLayout

            val navController = binding.navHostFragment.findNavController()

            if (drawerLayout.isOpen) {
                drawerLayout.close()
            } else if (!navController.popBackStack()) {
                finish()
            }
        }
    }

    private val pronunciationSynthesizer = PronunciationSynthesizer()

    override fun onCreate(savedInstanceState: Bundle?) {

        setupSplashScreen()

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    override fun onStart() {
        super.onStart()

        if (intent.action == ACTION_OPEN_TRANSLATION) {
            openTranslationWithId()
        } else if (intent.type == "text/plain") {
            openTranslateWithSourceText()
        }
    }

    override fun onDestroy() {
        pronunciationSynthesizer.shutdown()
        _binding = null
        super.onDestroy()
    }

    override fun pronounce(text: String, locale: Locale) {
        pronunciationSynthesizer.synthesizePronunciation(text, locale)
    }

    override fun stop() {
        pronunciationSynthesizer.stop()
    }

    val isNavigationDrawerOpen: Boolean
        get() = binding.drawerLayout.isOpen

    fun openNavigationDrawer() {
        binding.drawerLayout.open()
    }

    fun closeNavigationDrawer() {
        binding.drawerLayout.close()
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value
            }
            pronunciationSynthesizer.initialize(this@MainActivity) {
                viewModel.setShowSplashScreen(false)
            }
        }
    }

    private fun setupView() {
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.drawerNavigation.setNavigationItemSelectedListener { menuItem ->
            drawerMenuItemNavigate(menuItem)
            closeNavigationDrawer()
            return@setNavigationItemSelectedListener true
        }
    }

    private fun drawerMenuItemNavigate(menuItem: MenuItem) {
        val navController = binding.navHostFragment.findNavController()
        when (menuItem.itemId) {
            R.id.miTranslations -> {
                if (!navController.popBackStack(R.id.fragmentTranslationsList, false)) {
                    navController.navigate(R.id.fragmentTranslationsList)
                }
            }
            R.id.miSettings -> {
                if (!navController.popBackStack(R.id.fragmentSettings, false)) {
                    navController.navigate(R.id.fragmentSettings)
                }
            }
            else -> {
                throw IllegalArgumentException("Unknown Drawer MenuItem.")
            }
        }
    }

    private fun openTranslationWithId() {
        val translationId = intent.getLongExtra(EXTRA_TRANSLATION_ID, -1L)
        if (translationId == -1L) return

        val args = Bundle().apply {
            putLong("translationId", translationId)
        }
        binding.navHostFragment.findNavController().navigate(R.id.fragmentTranslationDetails, args)
        intent.removeExtra(EXTRA_TRANSLATION_ID)
    }

    private fun openTranslateWithSourceText() {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
            val args = Bundle().apply {
                putString(KEY_EXTERNAL_SOURCE_TEXT, text.trim().trim('"'))
            }
            binding.navHostFragment.findNavController().navigate(R.id.fragmentTranslate, args)
            intent.removeExtra(Intent.EXTRA_TEXT)
        }
    }
}