package com.sparkstudios.weatherapp.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sparkstudios.weatherapp.data.remote.WeatherApi
import com.sparkstudios.weatherapp.domain.location.LocationTracker
import com.sparkstudios.weatherapp.domain.repository.WeatherRepository
import com.sparkstudios.weatherapp.presentation.viewModels.WeatherViewModel
import com.sparkstudios.weatherapp.presentation.WeatherViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)
}

