package com.catvasiliy.mydic.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(true)
    val state = _state.asStateFlow()

    fun setShowSplashScreen(showSplashScreen: Boolean) {
        viewModelScope.launch {
            _state.update {
                showSplashScreen
            }
        }
    }
}