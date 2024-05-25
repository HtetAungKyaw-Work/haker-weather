package com.haker.hakerweather.repository

import com.haker.hakerweather.data.local.UserDao
import com.haker.hakerweather.data.model.Astronomy
import com.haker.hakerweather.data.model.AstronomyResponse
import com.haker.hakerweather.data.model.SportsResponse
import com.haker.hakerweather.data.model.User
import com.haker.hakerweather.data.model.Weather
import com.haker.hakerweather.data.remote.WeatherApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val userDao: UserDao
) {

    suspend fun search(q: String): Response<MutableList<Weather>> {
        return weatherApi.search(q)
    }

    suspend fun astronomy(q: String, date: String): Response<AstronomyResponse> {
        return weatherApi.astronomy(q, date)
    }

    suspend fun sports(q: String): Response<SportsResponse> {
        return weatherApi.sports(q)
    }

    suspend fun insertUser(user: User) = userDao.insert(user)

    fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    fun login(email: String, password: String) = userDao.login(email, password)
}