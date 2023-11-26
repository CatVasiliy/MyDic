package com.catvasiliy.mydic.di

import android.content.Context
import androidx.room.Room
import com.catvasiliy.mydic.data.local.database.TranslationDao
import com.catvasiliy.mydic.data.local.database.TranslationDatabase
import com.catvasiliy.mydic.data.remote.TranslateApi
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.use_case.DeleteTranslation
import com.catvasiliy.mydic.domain.use_case.GetTranslation
import com.catvasiliy.mydic.domain.use_case.GetTranslationsList
import com.catvasiliy.mydic.domain.use_case.InsertTranslation
import com.catvasiliy.mydic.domain.use_case.TranslationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTranslateApi(): TranslateApi {
        return Retrofit.Builder()
            .baseUrl("https://translate.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTranslationDatabase(@ApplicationContext context: Context): TranslationDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TranslationDatabase::class.java,
            name = "translation_database"
        ).build()
    }

    @Provides
    fun provideTranslationDao(translationDatabase: TranslationDatabase): TranslationDao {
        return translationDatabase.translationDao()
    }

    @Provides
    @Singleton
    fun provideTranslationUseCases(repository: TranslateRepository): TranslationUseCases {
        return TranslationUseCases(
            GetTranslationsList(repository),
            GetTranslation(repository),
            InsertTranslation(repository),
            DeleteTranslation(repository)
        )
    }
}