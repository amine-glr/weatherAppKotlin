package com.aminedglrglr.weatherapp.view

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aminedglrglr.weatherapp.R
import com.aminedglrglr.weatherapp.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET =getSharedPreferences(packageName, MODE_PRIVATE)
        SET= GET.edit()

        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)

        var cName= GET.getString("CityName", "istanbul")
        findCity.setText(cName)

        viewModel.refreshData(cName!!)

        getLiveData()
        refresh_layout.setOnRefreshListener {
            dataView.visibility=View.GONE
            tv_error.visibility=View.GONE
            pb_loading.visibility=View.GONE

            var cityName= GET.getString("CityName", cName)
            findCity.setText(cityName)
            viewModel.refreshData(cityName!!)
            refresh_layout.isRefreshing=false
        }
        searchbutton.setOnClickListener {
            val cityName = findCity.text.toString()
            SET.putString("CityName", cityName)
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: " + cityName)
        }

    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this, Observer { data ->
            data?.let {
                dataView.visibility= View.VISIBLE
                pb_loading.visibility=View.GONE
                degree.text= data.main.temp.toString() + "°C"
                Country.text= data.sys.country.toString()
                CityName.text= data.name.toString()
                humidity.text= "Humidity: " + data.main.humidity.toString()
                temp_max.text=  "Max Temp: " + data.main.tempMax.toString()
                temp_min.text=  "Min Temp: " +data.main.tempMin.toString()

                Glide.with(this).load("http://openweathermap.org/img/wn/" + data.weather.get(0).icon+ "10d@2x.png")
                    .into(iconWeather)


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