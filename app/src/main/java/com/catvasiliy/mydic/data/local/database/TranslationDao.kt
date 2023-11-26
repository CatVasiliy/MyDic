package com.catvasiliy.mydic.data.local.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedDefinition
import com.catvasiliy.mydic.data.local.database.model.CachedExample
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedSynonym
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationAggregate
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

        return translationId
    }

    @Transaction
    @Query("SELECT * FROM translation")
    abstract fun getTranslations(): Flow<List<CachedTranslation>>

    @Transaction
    @Query("SELECT * FROM translation WHERE id = :id")
    abstract suspend fun getTranslationById(id: Long): CachedTranslationAggregate

    @Transaction
    @Query("DELETE FROM translation WHERE id = :id")
    abstract suspend fun deleteTranslationById(id: Long)

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertMissingTranslation(missingTranslation: CachedMissingTranslation): Long

    @Query("SELECT * FROM missing_translation")
    abstract fun getMissingTranslations(): Flow<List<CachedMissingTranslation>>

    @Query("SELECT * FROM missing_translation WHERE id = :id")
    abstract suspend fun getMissingTranslationById(id: Long): CachedMissingTranslation

    @Query("DELETE FROM missing_translation WHERE id = :id")
    abstract suspend fun deleteMissingTranslationById(id: Long)
}
