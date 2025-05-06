package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.WeatherModel


interface WeatherRepository {
    suspend fun getWeather(city: String): Result<WeatherModel>
}