package com.aminedglrglr.weatherapp.service

import com.aminedglrglr.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=istanbul&appid=0eb4c59149aa1974a8c2eb218b37217f

interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&appid=0eb4c59149aa1974a8c2eb218b37217f")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>

}