package com.catvasiliy.mydic.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.drawerNavigation.setNavigationItemSelectedListener { menuItem ->
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
            }
            closeNavigationDrawer()
            return@setNavigationItemSelectedListener true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }


//        val alarmScheduler = AndroidAlarmScheduler(this)
//        alarmScheduler.schedule()
    }

    override fun onStart() {
        super.onStart()

        if (intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { text ->
                val args = Bundle().apply {
                    putString("sourceText", text.trim().trim('"'))
                }
                binding.navHostFragment.findNavController().navigate(R.id.fragmentTranslate, args)
                intent.removeExtra(Intent.EXTRA_TEXT)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    val isNavigationDrawerOpen: Boolean get() = binding.drawerLayout.isOpen

    fun openNavigationDrawer() {
        binding.drawerLayout.open()
    }

    fun closeNavigationDrawer() {
        binding.drawerLayout.close()
    }
}