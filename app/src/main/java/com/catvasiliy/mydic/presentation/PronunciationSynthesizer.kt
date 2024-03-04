package com.catvasiliy.mydic.presentation

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class PronunciationSynthesizer {

    private lateinit var textToSpeech: TextToSpeech
    private var isInitializedSuccessfully: Boolean = false

    private val utteranceId = "PronunciationSynthesizer"

    fun initialize(context: Context, onInitialized: () -> Unit) {
        if (this::textToSpeech.isInitialized) return

        textToSpeech = TextToSpeech(context) { result ->
            isInitializedSuccessfully = result == TextToSpeech.SUCCESS
            onInitialized()
        }
    }

    fun synthesizePronunciation(text: String, locale: Locale) {
        checkIfTextToSpeechInitialized()

        textToSpeech.language = locale

        val result = textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )

        println(result)
    }

    fun stop() {
        checkIfTextToSpeechInitialized()

        textToSpeech.stop()
    }

    fun shutdown() {
        if (!this::textToSpeech.isInitialized) throw UninitializedException()

        textToSpeech.shutdown()
    }

    private fun checkIfTextToSpeechInitialized() {
        if (!this::textToSpeech.isInitialized) throw UninitializedException()
        if (!isInitializedSuccessfully) throw InitializationFailureException()
    }

    inner class UninitializedException
        : RuntimeException("PronunciationSynthesizer is not initialized. Make sure to call initialize() before using.")

    inner class InitializationFailureException
        : RuntimeException("PronunciationSynthesizer failed to initialize.")
}