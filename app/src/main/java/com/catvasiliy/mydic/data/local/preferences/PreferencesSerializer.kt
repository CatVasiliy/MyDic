package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.Serializer
import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.domain.model.preferences.TranslationPreferences
import com.catvasiliy.mydic.domain.model.translation.Language
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

object PreferencesSerializer : Serializer<TranslationPreferences> {

    override val defaultValue: TranslationPreferences
        get() {
            val deviceLanguageCode = Locale.getDefault().language
            val defaultTargetLanguage = Language.fromCode(deviceLanguageCode) ?: Language.RUSSIAN

            val languagePreferences = LanguagePreferences(defaultTargetLanguage = defaultTargetLanguage)

            return TranslationPreferences(languagePreferences = languagePreferences)
        }

    override suspend fun readFrom(input: InputStream): TranslationPreferences {
        return try {
            Json.decodeFromString(
                deserializer = TranslationPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TranslationPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = TranslationPreferences.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}