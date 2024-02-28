package com.catvasiliy.mydic.data.remote.model.json_transformer

import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

object ApiDefinitionListTransformer
    : JsonTransformingSerializer<List<ApiDefinition>>(ListSerializer(ApiDefinition.serializer())) {

    override fun transformDeserialize(element: JsonElement): JsonElement {

        val definitionArray = element.jsonArray.flatMap { definitionElement ->

            val posPrimitive = definitionElement.jsonObject["pos"] ?: JsonNull

            val definitionEntryArray = definitionElement.jsonObject["entry"]?.jsonArray

            definitionEntryArray?.map { definitionEntryObject ->
                getDefinitionJsonObject(posPrimitive, definitionEntryObject)
            } ?: emptyList()
        }

        return JsonArray(definitionArray)
    }

    private fun getDefinitionJsonObject(
        posPrimitive: JsonElement,
        definitionEntryObject: JsonElement
    ): JsonObject {

        val glossPrimitive = definitionEntryObject.jsonObject["gloss"] ?: JsonNull

        val examplePrimitive = definitionEntryObject.jsonObject["example"] ?: JsonNull

        return JsonObject(
            mapOf(
                "gloss" to glossPrimitive,
                "pos" to posPrimitive,
                "example" to examplePrimitive
            )
        )
    }
}