package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.Serializer
import com.catvasiliy.mydic.domain.model.preferences.TranslationPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<TranslationPreferences> {

    override val defaultValue: TranslationPreferences = TranslationPreferences.getDefault()

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

    override suspend fun writeTo(t: TranslationPreferences, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = TranslationPreferences.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }

}