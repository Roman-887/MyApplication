package com.example.myapplication.ui.weather

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {

    val weather by viewModel.weather.observeAsState(initial = null)
    val error by viewModel.error.observeAsState(initial = "")

    val context = LocalContext.current

    var cityName by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (cityName.text.length >= 3) {
                    viewModel.fetchWeather(cityName.text)
                } else {
                    Toast.makeText(context, "Enter at least 3 characters", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(32.dp))

        weather?.let { currentWeather ->
            val description = when (currentWeather.description.lowercase()) {
                "clear sky" -> "ясне небо"
                "few clouds" -> "невелика хмарність"
                "scattered clouds" -> "розсіяні хмари"
                "broken clouds" -> "хмарно з проясненнями"
                "overcast clouds" -> "суцільна хмарність"
                "shower rain" -> "зливовий дощ"
                "rain" -> "дощ"
                "thunderstorm" -> "гроза"
                "snow" -> "сніг"
                "mist" -> "туман"
                else -> currentWeather.description
            }

            val temperatureText = stringResource(
                R.string.temperature_text,
                currentWeather.cityName,
                description,
                currentWeather.temperature
            )

            Text(text = temperatureText, style = MaterialTheme.typography.body1)
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.body1,
                color = Color.Red
            )
        }
    }
}