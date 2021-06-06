package com.aminedglrglr.weatherapp.view


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.lifecycle.ViewModelProviders
import com.aminedglrglr.weatherapp.R
import com.aminedglrglr.weatherapp.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var get: SharedPreferences
    private lateinit var set: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get =getSharedPreferences(packageName, MODE_PRIVATE)
        set= get.edit()

        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)

        val cName= get.getString("CityName", "istanbul")
        findCity.setText(cName)

        viewModel.refreshData(cName!!)

        getLiveData()
        refresh_layout.setOnRefreshListener {
            dataView.visibility=View.GONE
            tv_error.visibility=View.GONE
            pb_loading.visibility=View.GONE

            val cityName= get.getString("CityName", cName)
            findCity.setText(cityName)
            viewModel.refreshData(cityName!!)
            refresh_layout.isRefreshing=false
        }
        searchbutton.setOnClickListener {
            val cityName = findCity.text.toString()
            set.putString("CityName", cityName)
            set.apply()
            viewModel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: $cityName")
        }

    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this,  { data ->
            data?.let {
                dataView.visibility= View.VISIBLE
                pb_loading.visibility=View.GONE
                degree.text= data.main.temp.toString()
                Country.text= data.sys.country
                CityName.text= data.name
                humidity.text= data.main.humidity.toString()
                temp_max.text=  data.main.tempMax.toString()
                temp_min.text=   data.main.tempMin.toString()

                if(data.weather[0].description=="few clouds"){
                    Glide.with(this).load(R.drawable.cloud)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="clear sky"){
                    Glide.with(this).load(R.drawable.sunny)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="scattered clouds"){
                    Glide.with(this).load(R.drawable.stratuscumulus)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="broken clouds"){
                    Glide.with(this).load(R.drawable.cloud)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="shower rain"){
                    Glide.with(this).load(R.drawable.rain)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="rain"){
                    Glide.with(this).load(R.drawable.rain)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="thunderstorm"){
                    Glide.with(this).load(R.drawable.storm)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="mist"){
                    Glide.with(this).load(R.drawable.cumulus)
                        .into(iconWeather)
                }
                else if(data.weather[0].description=="snow"){
                    Glide.with(this).load(R.drawable.winter)
                        .into(iconWeather)
                }
                else{
                    Glide.with(this).load( "http://openweathermap.org/img/wn/" + data.weather[0].icon + ".png")
                        .into(iconWeather)
                }



            }
        })

        viewModel.weather_loading.observe(this, { load ->
            load?.let {
                if (it){
                        pb_loading.visibility= View.VISIBLE
                    tv_error.visibility=View.GONE
                    dataView.visibility=View.GONE
                }else{
                        pb_loading.visibility=View.GONE
                }
            }
        })

        viewModel.weather_error.observe(this, {error->
            error?.let {
                if (it){
                    tv_error.visibility= View.VISIBLE
                    dataView.visibility= View.GONE
                    pb_loading.visibility= View.GONE
                }else {
                    tv_error.visibility=View.GONE
                }
            }

        })
    }
}