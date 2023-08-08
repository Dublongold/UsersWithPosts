package com.example.userswithposts.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class RetrofitClient {
    private val mediaType = MediaType.get("application/json")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val converterFactory = json.asConverterFactory(mediaType)

    val client: NetworkApi = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(converterFactory)
        .build()
        .create(NetworkApi::class.java)
}