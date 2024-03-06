package com.catvasiliy.mydic.presentation.util.pronounce

import java.util.Locale

interface Pronouncer {
    fun pronounce(text: String, locale: Locale)
    fun stop()
}