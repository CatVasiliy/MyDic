package com.catvasiliy.mydic.data.remote.model.json_transformer

import com.catvasiliy.mydic.data.remote.model.ApiExample
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

object ApiExampleListTransformer
    : JsonTransformingSerializer<List<ApiExample>>(ListSerializer(ApiExample.serializer())) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonObject["example"] ?: element
    }
}