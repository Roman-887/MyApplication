package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.WeatherModel
import com.example.myapplication.domain.repository.WeatherRepository

class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): Result<WeatherModel> {
        return repository.getWeather(city)
    }
}