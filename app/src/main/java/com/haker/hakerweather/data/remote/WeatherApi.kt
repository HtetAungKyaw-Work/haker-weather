package com.haker.hakerweather.data.remote

import com.haker.hakerweather.data.model.AstronomyResponse
import com.haker.hakerweather.data.model.SportsResponse
import com.haker.hakerweather.data.model.Weather
import com.haker.hakerweather.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/search.json")
    suspend fun search(
        @Query("q") q: String = "",
        @Query("key") apiKey: String = API_KEY
    ): Response<MutableList<Weather>>

    @GET("/v1/astronomy.json")
    suspend fun astronomy(
        @Query("q") q: String = "",
        @Query("dt") date: String = "",
        @Query("key") apiKey: String = API_KEY
    ): Response<AstronomyResponse>

    @GET("/v1/sports.json")
    suspend fun sports(
        @Query("q") q: String = "",
        @Query("key") apiKey: String = API_KEY
    ): Response<SportsResponse>
}