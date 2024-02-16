package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTranslationsListUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    operator fun invoke(): Flow<List<Translation>> {
        return translateRepository.getTranslationsList()
    }
}