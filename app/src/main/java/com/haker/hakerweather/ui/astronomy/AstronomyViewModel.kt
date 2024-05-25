package com.haker.hakerweather.ui.astronomy

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haker.hakerweather.data.model.AstronomyResponse
import com.haker.hakerweather.data.model.Weather
import com.haker.hakerweather.repository.WeatherRepository
import com.haker.hakerweather.util.NetworkUtil
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AstronomyViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val data: MutableLiveData<Resource<AstronomyResponse>> = MutableLiveData()
    var astronomyResponse: AstronomyResponse? = null

    fun astronomy(q: String, date: String) = viewModelScope.launch {
        safeAstronomyCall(q, date)
    }

    private suspend fun safeAstronomyCall(q: String, date: String) {
        data.postValue(Resource.Loading())
        try {
            if (NetworkUtil.hasInternetConnection(context)) {
                val response = weatherRepository.astronomy(q, date)
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

    private fun handleWeatherResponse(response: Response<AstronomyResponse>): Resource<AstronomyResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (astronomyResponse == null)
                    astronomyResponse = resultResponse
                else {
                    astronomyResponse = resultResponse
                }
                return Resource.Success(astronomyResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}