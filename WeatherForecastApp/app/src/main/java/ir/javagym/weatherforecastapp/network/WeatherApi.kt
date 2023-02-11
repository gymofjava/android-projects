package ir.javagym.weatherforecastapp.network

import ir.javagym.weatherforecastapp.model.Weather
import ir.javagym.weatherforecastapp.model.WeatherObject
import ir.javagym.weatherforecastapp.utils.Constants
import ir.javagym.weatherforecastapp.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {
    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query : String,
        @Query("units") units: String = "imperial",
        @Query("appid") appid: String = API_KEY // your api key
                          ): Weather
}