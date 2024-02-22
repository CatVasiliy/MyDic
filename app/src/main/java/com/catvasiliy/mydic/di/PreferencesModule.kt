package com.catvasiliy.mydic.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.catvasiliy.mydic.data.local.preferences.PreferencesSerializer
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SETTINGS_FILE = "settings.json"

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<TranslationSendingPreferences> {
        return DataStoreFactory.create(
            serializer = PreferencesSerializer,
            produceFile = {
                context.dataStoreFile(SETTINGS_FILE)
            }
        )
    }
}