package com.catvasiliy.mydic.domain.use_case

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow

class GetTranslationsList(private val repository: TranslateRepository) {

    operator fun invoke(): Flow<List<Translation>> {
        return repository.getTranslationsList()
    }
}