package com.example.workOut.ui.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TTSManager {
    private var textToSpeech: TextToSpeech? = null

    fun initialize(context: Context) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(context) { status ->
                when (status) {
                    TextToSpeech.SUCCESS -> {
                        val result = textToSpeech?.setLanguage(Locale.TAIWAN)
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "Language not supported")
                        } else {
                            Log.d("TTS", "TTS initialized successfully with language: ${Locale.US}")
                        }
                    }
                    TextToSpeech.ERROR -> {
                        Log.e("TTS", "TextToSpeech initialization failed: ERROR")
                    }
                    TextToSpeech.ERROR_NETWORK -> {
                        Log.e("TTS", "TextToSpeech not initialized")
                    }
                    else -> {
                        Log.e("TTS", "TextToSpeech initialization failed with status: $status")
                    }
                }
            }
        }
    }

    fun speak(text: String) {
        val engines = textToSpeech?.engines
        Log.d("TTS", "Available TTS engines: ${engines?.joinToString { it.name }}")
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}