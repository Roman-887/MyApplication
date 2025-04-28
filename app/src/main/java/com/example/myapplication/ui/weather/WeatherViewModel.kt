package com.example.myapplication.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.WeatherModel
import com.example.myapplication.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.launch

class WeatherViewModel(private val getWeatherUseCase: GetWeatherUseCase) : ViewModel() {

    private val _weather = MutableLiveData<WeatherModel?>()
    val weather: LiveData<WeatherModel?> get() = _weather

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            val result = getWeatherUseCase(city)
            result
                .onSuccess { _weather.postValue(it) }
                .onFailure { _error.postValue(it.localizedMessage ?: "Unknown error") }
        }
    }
}