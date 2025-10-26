package com.sparkstudios.weatherapp.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparkstudios.weatherapp.domain.Resource
import com.sparkstudios.weatherapp.domain.location.LocationTracker
import com.sparkstudios.weatherapp.domain.repository.WeatherRepository
import com.sparkstudios.weatherapp.presentation.viewModels.WeatherViewModel.WeatherIntent
import com.sparkstudios.weatherapp.presentation.viewModels.WeatherViewModel.WeatherState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherViewModelRxJava @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    private val disposables = CompositeDisposable()

    fun processWeatherIntent(intent: WeatherIntent) {
        when (intent) {
            WeatherIntent.LoadWeather -> loadWeatherInfo()
        }
    }

    private fun loadWeatherInfo() = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        val location = locationTracker.getCurrentLocation()
        if (location == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
            return@launch
        } else {
            val disposable = repository.getWeatherDataSingle(location.latitude, location.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    weatherInfo = result.data,
                                    isLoading = false
                                )
                            }
                        }

                        is Resource.Error -> _state.update {
                            it.copy(
                                weatherInfo = null,
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }, { throwable ->
                    _state.update {
                        it.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = throwable.message
                        )
                    }
                })

            disposables.add(disposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
