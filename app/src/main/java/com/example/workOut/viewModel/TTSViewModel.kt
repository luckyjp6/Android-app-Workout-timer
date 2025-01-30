package com.example.workOut.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.workOut.ui.speech.TTSManager

class TTSViewModel(application: Application) : AndroidViewModel(application) {
    init {
        TTSManager.initialize(application.applicationContext)
    }

    fun speak(text: String) {
        TTSManager.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        TTSManager.shutdown()
    }
}