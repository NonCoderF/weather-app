package com.sparkstudios.weatherapp.di

import com.sparkstudios.weatherapp.data.repository.WeatherRepositoryImpl
import com.sparkstudios.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
