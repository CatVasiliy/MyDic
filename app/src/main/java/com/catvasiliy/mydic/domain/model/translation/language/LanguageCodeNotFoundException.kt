package com.catvasiliy.mydic.domain.model.translation.language

class LanguageCodeNotFoundException(languageCode: String)
    : IllegalArgumentException("Language with code \"$languageCode\" not found.")