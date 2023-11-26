package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.Serializer
import com.catvasiliy.mydic.domain.model.settings.SendTranslationPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<SendTranslationPreferences> {

    override val defaultValue: SendTranslationPreferences
        get() = SendTranslationPreferences()

    override suspend fun readFrom(input: InputStream): SendTranslationPreferences {
        return try {
            Json.decodeFromString(
                deserializer = SendTranslationPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SendTranslationPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SendTranslationPreferences.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}