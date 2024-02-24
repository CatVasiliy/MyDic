package com.catvasiliy.mydic.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedDefinition
import com.catvasiliy.mydic.data.local.database.model.CachedExample
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedSynonym
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationAggregate
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationForSending
import com.catvasiliy.mydic.domain.model.translation.Language
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TranslationDao {

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertCachedTranslation(translation: CachedTranslation): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertAlternativeTranslation(
        alternativeTranslation: CachedAlternativeTranslation
    ): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertSynonym(synonym: CachedSynonym)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertDefinition(definition: CachedDefinition)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertExample(example: CachedExample)

    @Transaction
    open suspend fun insertTranslation(translation: CachedTranslationAggregate): Long {
        val translationId = insertCachedTranslation(translation.translation)

        translation.alternativeTranslations.forEach { alternativeAggregate ->
            val alternativeTranslationWithId = alternativeAggregate
                .alternativeTranslation
                .copy(translationId = translationId)

            val alternativeTranslationId = insertAlternativeTranslation(
                alternativeTranslationWithId
            )

            alternativeAggregate.synonyms.forEach { synonym ->
                val synonymWithId = synonym.copy(
                    alternativeTranslationId = alternativeTranslationId
                )
                insertSynonym(synonymWithId)
            }
        }

        translation.definitions.forEach { definition ->
            val definitionWithId = definition.copy(translationId = translationId)
            insertDefinition(definitionWithId)
        }

        translation.examples.forEach { example ->
            val exampleWithId = example.copy(translationId = translationId)
            insertExample(exampleWithId)
        }

        insertTranslationForSending(CachedTranslationForSending(id = translationId))

        return translationId
    }

    @Transaction
    open suspend fun deleteTranslation(id: Long) {
        deleteTranslationForSendingById(id)
        deleteTranslationById(id)
    }

    @Transaction
    @Query("SELECT * FROM translation")
    abstract fun getTranslations(): Flow<List<CachedTranslation>>

    @Query("SELECT id FROM translation")
    abstract suspend fun getTranslationIds(): List<Long>

    @Transaction
    @Query("SELECT * FROM translation WHERE id = :id")
    abstract suspend fun getTranslationById(id: Long): CachedTranslationAggregate

    @Transaction
    @Query("SELECT * FROM translation " +
                "WHERE sourceText = :sourceText " +
                    "AND sourceLanguage = :sourceLanguage " +
                    "AND targetLanguage = :targetLanguage")
    abstract suspend fun getUniqueTranslation(
        sourceText: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): CachedTranslationAggregate?

    @Transaction
    @Query("SELECT * FROM translation " +
                "WHERE sourceText = :sourceText " +
                    "AND targetLanguage = :targetLanguage")
    abstract suspend fun getUniqueTranslationNoSourceLanguage(
        sourceText: String,
        targetLanguage: Language
    ): CachedTranslationAggregate?

    @Transaction
    @Query("DELETE FROM translation WHERE id = :id")
    abstract suspend fun deleteTranslationById(id: Long)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertMissingTranslation(missingTranslation: CachedMissingTranslation): Long

    @Query("SELECT * FROM missing_translation")
    abstract fun getMissingTranslations(): Flow<List<CachedMissingTranslation>>

    @Query("SELECT * FROM missing_translation WHERE id = :id")
    abstract suspend fun getMissingTranslationById(id: Long): CachedMissingTranslation

    @Query("SELECT * FROM missing_translation " +
                "WHERE sourceText = :sourceText " +
                    "AND sourceLanguage = :sourceLanguage " +
                    "AND targetLanguage = :targetLanguage")
    abstract suspend fun getUniqueMissingTranslation(
        sourceText: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): CachedMissingTranslation?

    @Query("DELETE FROM missing_translation WHERE id = :id")
    abstract suspend fun deleteMissingTranslationById(id: Long)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertTranslationForSending(
        translationForSending: CachedTranslationForSending
    )

    @Query("SELECT * FROM translation_for_sending")
    abstract suspend fun getTranslationsForSendingList(): List<CachedTranslationForSending>

    @Query("DELETE FROM translation_for_sending WHERE id = :id")
    abstract suspend fun deleteTranslationForSendingById(id: Long)
}
