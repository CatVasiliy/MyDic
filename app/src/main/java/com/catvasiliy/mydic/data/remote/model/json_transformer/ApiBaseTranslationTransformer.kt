package com.catvasiliy.mydic.data.remote.model.json_transformer

import com.catvasiliy.mydic.data.remote.model.ApiBaseTranslation
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object ApiBaseTranslationTransformer
    : JsonTransformingSerializer<ApiBaseTranslation>(ApiBaseTranslation.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {

        val sentencesArray = element.jsonArray

        val origPrimitive = sentencesArray[0].jsonObject["orig"] ?: JsonNull
        val transPrimitive = sentencesArray[0].jsonObject["trans"] ?: JsonNull
        val srcTranslitPrimitive = sentencesArray[1].jsonObject["src_translit"] ?: JsonNull

        return buildJsonObject {
            put("orig", origPrimitive)
            put("trans", transPrimitive)
            put("src_translit", srcTranslitPrimitive)
        }
    }
}