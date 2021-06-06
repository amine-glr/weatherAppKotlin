package com.aminedglrglr.weatherapp.service

import com.aminedglrglr.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {
    //https://api.openweathermap.org/data/2.5/weather?q=istanbul&appid=0eb4c59149aa1974a8c2eb218b37217f

    private val BASE_URL= "https://api.openweathermap.org/"

    private val api= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)


     fun getDataService(cityName: String): Single<WeatherModel>{
         return api.getData(cityName)
     }

}