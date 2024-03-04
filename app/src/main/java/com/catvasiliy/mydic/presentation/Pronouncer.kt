package com.catvasiliy.mydic.presentation

import java.util.Locale

interface Pronouncer {
    fun pronounce(text: String, locale: Locale)
    fun stop()
}