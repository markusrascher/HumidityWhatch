package com.faramiki.humiditywhatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import java.time.LocalDate

class MainViewModel : ViewModel()
{
    private var currentindex: Int = 0
    private var dummyData: MutableList<WeatherDataPoint> = createDummyData()
    private var selectedValue: MutableLiveData<WeatherDataPoint> = MutableLiveData()

    private var dateFromEpochDays: MutableLiveData<Long> = MutableLiveData(18820)
    private var dateToEpochDays:  MutableLiveData<Long> = MutableLiveData()

    init {
        dateFromEpochDays.value = LocalDate.now().toEpochDay()
        dateToEpochDays.value = LocalDate.now().toEpochDay()
    }


    fun setDateFromEpochDays(dateFromEpochDays: Long)
    {
        this.dateFromEpochDays.value = dateFromEpochDays
    }

    fun setDateToEpochDays(dateToEpochDays: Long)
    {
        this.dateToEpochDays.value = dateToEpochDays
    }

    fun getDateFromEpochDays(): LiveData<Long>{
        return dateFromEpochDays
    }

    fun getDateToEpochDays(): LiveData<Long>{
        return dateToEpochDays
    }

    fun selectValue() {
        selectedValue.value = dummyData[currentindex]
        currentindex ++
        if(currentindex == dummyData.size)
        {
            currentindex = 0
        }
    }

    fun getValue(): LiveData<WeatherDataPoint>{
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

        dummyData.add(WeatherDataPoint(1627671935, 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint(1627758335, 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint(1627844735, 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint(1627971935, 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(1628071935, 18.5f, 18f, 82f, 55f))

        return dummyData

    }

}
