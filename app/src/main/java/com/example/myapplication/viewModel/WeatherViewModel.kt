package com.example.myapplication.viewModel

import com.example.myapplication.data.WeatherResponse
import com.example.myapplication.data.WeatherApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {

    val weatherData = MutableLiveData<WeatherResponse?>()
    val errorMessage = MutableLiveData<String>()

    private val apiKey = "eaf4e4f9a93036c2b80a9658431c5d58"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(WeatherApi::class.java)

    fun getWeather(city: String) {
        service.getWeather(city, apiKey, "metric").enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    weatherData.postValue(response.body())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Невідома помилка"
                    errorMessage.postValue("Не вдалося отримати дані. ${response.code()} ${errorBody}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                errorMessage.postValue("Помилка: ${t.message}")
            }
        })
    }
}