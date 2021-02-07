package com.yj.kidcentivize.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class ApiStatus { LOADING, ERROR, DONE }

interface PlzmaService {

    @GET("kidcode")
    suspend fun getKidCode(
        @Query("name") name: String,
    ): Kid

    @GET("submitkidcode")
    suspend fun submitKidCode(
        @Query("code") code: String,
        @Query("name") name: String
    ): Parent


    companion object {
        private const val BASE_URL = "https://plzma.herokuapp.com/api/"

        fun create(): PlzmaService {

            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(PlzmaService::class.java)
        }
    }
}
