package com.sparkstudios.weatherapp.di

import android.app.Activity
import android.app.Application
import com.sparkstudios.weatherapp.WeatherApp
import com.sparkstudios.weatherapp.presentation.MainActivity
import com.sparkstudios.weatherapp.presentation.WeatherViewModel
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
@Component(
    modules = [AppModule::class, LocationModule::class, RepositoryModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(activity: MainActivity)
}


