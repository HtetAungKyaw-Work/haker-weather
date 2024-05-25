package com.haker.hakerweather.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haker.hakerweather.data.model.User
import com.haker.hakerweather.data.model.Weather
import com.haker.hakerweather.repository.WeatherRepository
import com.haker.hakerweather.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun login(email: String, password: String) = weatherRepository.login(email, password)

}