package com.sparkstudios.weatherapp.domain.repository

import com.sparkstudios.weatherapp.domain.weather.WeatherInfo
import com.sparkstudios.weatherapp.domain.Resource

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}