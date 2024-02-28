package com.catvasiliy.mydic.data.remote.model

import com.catvasiliy.mydic.data.remote.model.json_transformer.ApiAlternativeTranslationListTransformer
import com.catvasiliy.mydic.data.remote.model.json_transformer.ApiDefinitionListTransformer
import com.catvasiliy.mydic.data.remote.model.json_transformer.ApiExampleListTransformer
import com.catvasiliy.mydic.data.remote.model.json_transformer.ApiBaseTranslationTransformer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiTranslation(

    @SerialName("sentences")
    @Serializable(with = ApiBaseTranslationTransformer::class)
    val baseTranslation: ApiBaseTranslation,

    @SerialName("src")
    val sourceLanguageCode: String,

    @SerialName("dict")
    @Serializable(with = ApiAlternativeTranslationListTransformer::class)
    val alternativeTranslations: List<ApiAlternativeTranslation> = emptyList(),

    @SerialName("definitions")
    @Serializable(with = ApiDefinitionListTransformer::class)
    val definitions: List<ApiDefinition> = emptyList(),

    @SerialName("examples")
    @Serializable(with = ApiExampleListTransformer::class)
    val examples: List<ApiExample> = emptyList()
)