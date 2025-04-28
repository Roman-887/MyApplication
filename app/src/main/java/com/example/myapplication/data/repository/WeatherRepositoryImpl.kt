package com.example.myapplication.data.repository

import com.example.myapplication.data.api.WeatherApi
import com.example.myapplication.domain.model.WeatherModel
import com.example.myapplication.domain.repository.WeatherRepository

class WeatherRepositoryImpl(private val api: WeatherApi) : WeatherRepository {
    override suspend fun getWeather(city: String): Result<WeatherModel> {
        return try {
            val response = api.getWeather(city, "eaf4e4f9a93036c2b80a9658431c5d58")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val description = body.weather.firstOrNull()?.description ?: "no description"
                    Result.success(
                        WeatherModel(
                            cityName = body.name,
                            temperature = body.main.temp,
                            description = description
                        )
                    )
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}