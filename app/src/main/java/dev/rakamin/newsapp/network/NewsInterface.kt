package dev.rakamin.newsapp.network

import dev.rakamin.newsapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "f6698863239348cabc59b9876a9a060f"

interface NewsInterface {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = "id",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse


    @GET("v2/everything")
    suspend fun getAllNews(
        @Query("q") query: String = "indonesia",
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse
}