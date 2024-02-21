package com.catvasiliy.mydic.data.remote

import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {

    @GET("translate_a/single?client=gtx&dj=1&dt=t&dt=rm&dt=bd&dt=ex&dt=md")
    suspend fun getTranslation(
        @Query("q") sourceText: String,
        @Query("sl") sourceLanguage: String,
        @Query("tl") targetLanguage: String
    ): ApiTranslation
}