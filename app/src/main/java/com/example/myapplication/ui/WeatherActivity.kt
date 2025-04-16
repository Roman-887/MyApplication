package com.example.myapplication.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.util.DebounceTextWatcher
import com.example.myapplication.viewModel.WeatherViewModel

class WeatherActivity : AppCompatActivity() {

    private var weatherText: TextView? = null
    private var inputCity: EditText? = null

    private val viewModel: WeatherViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inputCity = EditText(this).apply {
            hint = "Введіть місто"
            textSize = 18f
            setPadding(20, 50, 20, 20)
        }

        weatherText = TextView(this).apply {
            textSize = 20f
            setPadding(20, 120, 20, 20)
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(inputCity)
            addView(weatherText)
        }

        setContentView(layout)

        inputCity?.addTextChangedListener(
            DebounceTextWatcher { city ->
                viewModel.getWeather(city)
            })

        viewModel.weatherData.observe(this) { weather ->
            if (weather != null) {
                val description = when (weather.weather.firstOrNull()?.description?.lowercase()) {
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
                    else -> weather.weather.firstOrNull()?.description ?: "немає опису"
                }

                val result = getString(
                    R.string.temperature_text,
                    weather.name,
                    description,
                    weather.main.temp
                )
                weatherText?.text = result
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            weatherText?.text = error
        }
    }
}