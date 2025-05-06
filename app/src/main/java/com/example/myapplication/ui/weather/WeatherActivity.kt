package com.example.myapplication.ui.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.api.RetrofitInstance
import com.example.myapplication.data.repository.WeatherRepositoryImpl
import com.example.myapplication.domain.usecase.GetWeatherUseCase

class WeatherActivity : ComponentActivity() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = RetrofitInstance.api
        val repository = WeatherRepositoryImpl(api)
        val useCase = GetWeatherUseCase(repository)
        val factory = WeatherViewModelFactory(useCase)
        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        setContent {
            WeatherScreen(viewModel)
        }
    }
}