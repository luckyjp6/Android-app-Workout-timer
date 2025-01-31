package com.example.workOut.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingViewModel :ViewModel() {
    private var _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun setIsDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
    fun changeIsDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}