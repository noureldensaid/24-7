package com.example.news.data.remote

import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getCategory(
        @Query("language") language: String = "en",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):NewsResponse

    @GET("v2/everything")
    suspend fun getSearchResults(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): NewsResponse


}