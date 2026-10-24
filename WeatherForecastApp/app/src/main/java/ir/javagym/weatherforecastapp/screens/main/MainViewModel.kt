package ir.javagym.weatherforecastapp.screens.main

import androidx.lifecycle.ViewModel
import ir.javagym.weatherforecastapp.data.DataOrException
import ir.javagym.weatherforecastapp.model.Weather
import ir.javagym.weatherforecastapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel(){

        suspend fun getWeatherData(city: String, units: String)
        : DataOrException<Weather, Boolean, Exception> {
            return repository.getWeather(cityQuery = city, units = units)

        }




}