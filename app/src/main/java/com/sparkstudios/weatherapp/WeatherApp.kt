package com.sparkstudios.weatherapp

import android.app.Application
import com.sparkstudios.weatherapp.di.AppComponent
import com.sparkstudios.weatherapp.di.DaggerAppComponent

class WeatherApp : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}
