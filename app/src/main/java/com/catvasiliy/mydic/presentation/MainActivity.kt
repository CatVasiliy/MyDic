package com.catvasiliy.mydic.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
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
            val isNotLastBackStackEntry = navController.backQueue.count { entry ->
                entry.destination !is NavGraph
            } > 1

            if (drawerLayout.isOpen) {
                drawerLayout.close()
            } else if (isNotLastBackStackEntry) {
                navController.popBackStack()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
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