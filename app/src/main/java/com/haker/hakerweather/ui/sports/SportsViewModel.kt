package com.haker.hakerweather.ui.sports

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haker.hakerweather.data.model.AstronomyResponse
import com.haker.hakerweather.data.model.SportsResponse
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
class SportsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val data: MutableLiveData<Resource<SportsResponse>> = MutableLiveData()
    var sportsResponse: SportsResponse? = null

    fun sports(q: String) = viewModelScope.launch {
        safeSportsCall(q)
    }

    private suspend fun safeSportsCall(q: String) {
        data.postValue(Resource.Loading())
        try {
            if (NetworkUtil.hasInternetConnection(context)) {
                val response = weatherRepository.sports(q)
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

    private fun handleWeatherResponse(response: Response<SportsResponse>): Resource<SportsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (sportsResponse == null)
                    sportsResponse = resultResponse
                else {
                    sportsResponse = resultResponse
                }
                return Resource.Success(sportsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}