package com.faramiki.humiditywhatch.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faramiki.humiditywhatch.Fals22ApiClient
import com.faramiki.humiditywhatch.entities.Fals22DataModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.network.RetrofitFals22Interface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherDataRepository {

    private var fals22Interface: RetrofitFals22Interface? = null

    init {
        fals22Interface = Fals22ApiClient.getApiClient().create(RetrofitFals22Interface::class.java)
    }

    fun getOnlineWeatherData(dayInEpochHours: Long): LiveData<List<WeatherDataPoint>?>{
        val data = MutableLiveData<List<WeatherDataPoint>?>()

        fals22Interface?.getDataPoints()?.enqueue(object : Callback<List<Fals22DataModel>> {

            override fun onFailure(call: Call<List<Fals22DataModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<List<Fals22DataModel>>,
                response: Response<List<Fals22DataModel>>
            ) {

                val res = response.body()
                if (response.code() == 200 &&  res!=null){
                    data.value = res.map{x -> x.toWeatherDataPoint(dayInEpochHours)}
                }else{
                    data.value = null
                }
            }
        })

        return data
    }
}