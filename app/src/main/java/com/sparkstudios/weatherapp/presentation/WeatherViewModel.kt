package com.sparkstudios.weatherapp.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparkstudios.weatherapp.domain.Resource
import com.sparkstudios.weatherapp.domain.location.LocationTracker
import com.sparkstudios.weatherapp.domain.repository.WeatherRepository
import com.sparkstudios.weatherapp.domain.weather.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    sealed class WeatherIntent {
        object LoadWeather : WeatherIntent()
    }

    data class WeatherState(
        val weatherInfo: WeatherInfo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    fun processWeatherIntent(intent: WeatherIntent) {
        when (intent) {
            WeatherIntent.LoadWeather -> loadWeatherInfo()
        }
    }

    private fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }

            locationTracker.getCurrentLocation()?.let { location ->
                when (val result = repository.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                weatherInfo = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                weatherInfo = null,
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            } ?: run {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                    )
                }
            }
        }
    }
}

