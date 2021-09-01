package com.faramiki.humiditywhatch.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faramiki.humiditywhatch.Fals22ApiClient
import com.faramiki.humiditywhatch.entities.Fals22DataModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.network.RetrofitFals22Interface
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class WeatherDataRepository {

    private var fals22Interface: RetrofitFals22Interface? = null
    private var loadedWeatherDataPoints: MutableLiveData<List<WeatherDataPoint>>
        = MutableLiveData(listOf())
    private var loadedWeatherDataPointsInternal: MutableList<WeatherDataPoint> = mutableListOf()

    init {
        fals22Interface = Fals22ApiClient.getApiClient().create(RetrofitFals22Interface::class.java)
    }

    fun getLoadedValues(): LiveData<List<WeatherDataPoint>>{
        return loadedWeatherDataPoints
    }

    fun getWeatherData(dateFrom: LocalDate, dateTo: LocalDate){
        var currentDate = dateTo

        while (currentDate >= dateFrom){
            if(loadedWeatherDataPoints.value!!.none { x -> x.timestamp == currentDate.toEpochHours() })
            {
                loadWeatherDataPointsFromWeb(currentDate)
            }

            currentDate = currentDate.minusDays(1)
        }
    }

    private fun loadWeatherDataPointsFromWeb(day: LocalDate) {
        fals22Interface?.getDataPoints("10342", day.toString())?.enqueue(object : Callback<List<Fals22DataModel>> {

            override fun onFailure(call: Call<List<Fals22DataModel>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<List<Fals22DataModel>>,
                response: Response<List<Fals22DataModel>>
            ) {

                val res = response.body()
                if (response.code() == 200 &&  res!=null) {


                    res.forEach {
                            value ->
                        run {
                            val lodadedWeatherDataPoint = value.toWeatherDataPoint(day)

                            if (loadedWeatherDataPointsInternal.none { x -> x.timestamp == lodadedWeatherDataPoint.timestamp }) {
                                loadedWeatherDataPointsInternal.add(lodadedWeatherDataPoint)
                            }
                        }
                    }
                    loadedWeatherDataPointsInternal.sortBy { it.timestamp }
                    loadedWeatherDataPoints.value = loadedWeatherDataPointsInternal
                }
            }
        })
    }
}