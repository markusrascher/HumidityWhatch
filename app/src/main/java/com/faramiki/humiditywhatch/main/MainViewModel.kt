package com.faramiki.humiditywhatch.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel()
{
    private var currentindex: Int = 0
    var dummyData: MutableList<WeatherDataPoint> = createDummyData()
    private var selectedValue: MutableLiveData<CharSequence> = MutableLiveData()

    fun selectValue() {
        val filtered = dummyData[currentindex]
        selectedValue.value = filtered.temp.toString()
        currentindex ++
        if(currentindex == dummyData.size)
        {
            currentindex = 0
        }
    }

    fun getValue(): LiveData<CharSequence>{
        return selectedValue
    }





    /*

    fun getByDate(startDate: Long, endDate: Long): List<WeatherDataPoint> {
        return dummyData.filter { point -> (point.timestamp >= startDate) and (point.timestamp <= endDate) }
    }

    */

    ////////////////////////////////////////////////////////
    // Dummy Data (Will be replaced by loading from database
    private fun createDummyData(): MutableList<WeatherDataPoint> {
        val dummyData: MutableList<WeatherDataPoint> = mutableListOf()

        dummyData.add(WeatherDataPoint(1627671935, 20.5f, 78.0f))
        dummyData.add(WeatherDataPoint(1627758335, 20.8f, 77.0f))
        dummyData.add(WeatherDataPoint(1627844735, 21.5f, 50.0f))
        dummyData.add(WeatherDataPoint(1627971935, 19.5f, 60.0f))
        dummyData.add(WeatherDataPoint(1628071935, 18.5f, 88.0f))

        return dummyData

    }

}
