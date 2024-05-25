package com.haker.hakerweather.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haker.hakerweather.data.model.User
import com.haker.hakerweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    fun getUserByEmail(email: String) = weatherRepository.getUserByEmail(email)

    fun saveUser(user: User) {
        viewModelScope.launch {
            weatherRepository.insertUser(user)
        }
    }
}