package com.sparkstudios.weatherapp.data.repository

import com.sparkstudios.weatherapp.data.mappers.toWeatherInfo
import com.sparkstudios.weatherapp.data.remote.WeatherApi
import com.sparkstudios.weatherapp.domain.Resource
import com.sparkstudios.weatherapp.domain.repository.WeatherRepository
import com.sparkstudios.weatherapp.domain.weather.WeatherInfo
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override fun getWeatherDataSingle(
        lat: Double,
        lon: Double
    ): Single<Resource<WeatherInfo>> {
        return Single.fromCallable {
            runBlocking {
                getWeatherData(lat, lon)
            }
        }
    }
}
