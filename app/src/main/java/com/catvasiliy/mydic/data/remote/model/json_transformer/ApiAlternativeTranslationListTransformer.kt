package com.catvasiliy.mydic.data.remote.model.json_transformer

import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object ApiAlternativeTranslationListTransformer
    : JsonTransformingSerializer<List<ApiAlternativeTranslation>>(
    ListSerializer(ApiAlternativeTranslation.serializer())
) {

    override fun transformDeserialize(element: JsonElement): JsonElement {

        val alternativeArray = element.jsonArray.flatMap { alternativeElement ->

            val posPrimitive = alternativeElement.jsonObject["pos"] ?: JsonNull

            val alternativeEntryArray = alternativeElement.jsonObject["entry"]?.jsonArray

            alternativeEntryArray?.map { alternativeEntryElement ->
                getAlternativeTranslationJsonObject(posPrimitive, alternativeEntryElement)
            } ?: emptyList()
        }

        return JsonArray(alternativeArray)
    }

    private fun getAlternativeTranslationJsonObject(
        posPrimitive: JsonElement,
        alternativeEntryElement: JsonElement
    ): JsonObject {

        val wordPrimitive = alternativeEntryElement.jsonObject["word"] ?: JsonNull

        val reverseTranslationArray = alternativeEntryElement.jsonObject["reverse_translation"]
            ?: JsonNull

        return JsonObject(
            mapOf(
                "word" to wordPrimitive,
                "pos" to posPrimitive,
                "reverse_translation" to reverseTranslationArray
            )
        )
    }
}