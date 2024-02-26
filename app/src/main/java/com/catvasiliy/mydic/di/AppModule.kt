package com.catvasiliy.mydic.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.catvasiliy.mydic.data.local.database.TranslationDao
import com.catvasiliy.mydic.data.local.database.TranslationDatabase
import com.catvasiliy.mydic.data.remote.TranslateApi
import com.catvasiliy.mydic.presentation.settings.translation_sending.Notifier
import com.catvasiliy.mydic.presentation.settings.translation_sending.TranslationNotifier
import com.catvasiliy.mydic.presentation.settings.translation_sending.TranslationSendingAlarmScheduler
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTranslateApi(): TranslateApi {
        val mediaType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://translate.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(mediaType))
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
    fun provideTranslationSendingAlarmScheduler(
        @ApplicationContext context: Context
    ): TranslationSendingAlarmScheduler {
        return TranslationSendingAlarmScheduler(context)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(NotificationManager::class.java)
    }

    @Provides
    @TranslationNotifierQualifier
    fun provideNotifier(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): Notifier {
        return TranslationNotifier(
            context = context,
            notificationManager = notificationManager
        )
    }
}