package com.sparkstudios.weatherapp.di

import com.sparkstudios.weatherapp.data.location.DefaultLocationTracker
import com.sparkstudios.weatherapp.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        defaultLocationTracker: DefaultLocationTracker
    ): LocationTracker
}
