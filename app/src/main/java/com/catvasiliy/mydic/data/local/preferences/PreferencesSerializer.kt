package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.Serializer
import com.catvasiliy.mydic.domain.model.settings.TranslationSendingPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<TranslationSendingPreferences> {

    override val defaultValue: TranslationSendingPreferences
        get() = TranslationSendingPreferences()

    override suspend fun readFrom(input: InputStream): TranslationSendingPreferences {
        return try {
            Json.decodeFromString(
                deserializer = TranslationSendingPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TranslationSendingPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = TranslationSendingPreferences.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}