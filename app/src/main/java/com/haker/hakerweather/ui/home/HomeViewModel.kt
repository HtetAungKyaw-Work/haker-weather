package com.haker.hakerweather.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haker.hakerweather.data.model.Weather
import com.haker.hakerweather.repository.WeatherRepository
import com.haker.hakerweather.util.NetworkUtil.Companion.hasInternetConnection
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val data: MutableLiveData<Resource<MutableList<Weather>>> = MutableLiveData()
    var weatherResponse: MutableList<Weather>? = null

    fun search(q: String) = viewModelScope.launch {
        Log.i("city_name", q)
        safeSearchCall(q)
    }

    private suspend fun safeSearchCall(q: String) {
        data.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = weatherRepository.search(q)
                data.postValue(handleWeatherResponse(response))
            }
            else {
                data.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> data.postValue(Resource.Error("Network Failure"))
                else -> data.postValue(Resource.Error("Conversion Error"))
            }

            Log.e("error", ex.message.toString())
        }
    }

    private fun handleWeatherResponse(response: Response<MutableList<Weather>>): Resource<MutableList<Weather>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (weatherResponse == null)
                    weatherResponse = resultResponse
                else {
                    weatherResponse = resultResponse
                }
                return Resource.Success(weatherResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}