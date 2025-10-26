package com.sparkstudios.weatherapp.domain.repository

import com.sparkstudios.weatherapp.domain.Resource
import com.sparkstudios.weatherapp.domain.weather.WeatherInfo
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
    fun getWeatherDataSingle(lat: Double, lon: Double): Single<Resource<WeatherInfo>>
}