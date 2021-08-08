package com.faramiki.humiditywhatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


private const val FACTOR_DAYS_TO_MILLISEC = 86400000L

class MainViewModel : ViewModel()
{
    //Date range
    private var dateFromEpochDays: MutableLiveData<Long> = MutableLiveData()
    private var dateToEpochDays:  MutableLiveData<Long> = MutableLiveData()

    //Weather data points in range
    private var inRangeValues: MutableLiveData<List<WeatherDataPoint>> = MutableLiveData()

    // Dummy data points
    private var dummyData: List<WeatherDataPoint> = createDummyData()

    init {
        dateFromEpochDays.value = LocalDate.now().toEpochDay()
        dateToEpochDays.value = LocalDate.now().toEpochDay()
        setInRangeValues()
    }

    ///////////////////////////////////
    // Getters / Setters for date range
    ///////////////////////////////////

    fun setDateFromEpochDays(dateFromEpochDays: Long)
    {
        this.dateFromEpochDays.value = dateFromEpochDays
        setInRangeValues()
    }

    fun setDateToEpochDays(dateToEpochDays: Long)
    {
        this.dateToEpochDays.value = dateToEpochDays
        setInRangeValues()
    }

    fun getDateFromEpochDays(): LiveData<Long>{
        return dateFromEpochDays
    }

    fun getDateToEpochDays(): LiveData<Long>{
        return dateToEpochDays
    }


    ///////////////////////////////////
    // Getter / Setter for InRange Values
    ///////////////////////////////////

    fun getInRangeValues(): LiveData<List<WeatherDataPoint>>{
        return inRangeValues
    }

    private fun setInRangeValues() {

        val fromTime = LocalDateTime.ofEpochSecond(dateFromEpochDays.value!! * 86400, 0, ZoneOffset.ofHours(0))
        val toTime = LocalDateTime.ofEpochSecond((dateToEpochDays.value!! + 1) * 86400, 0, ZoneOffset.ofHours(0))

        val fromTimeEpochMilli = fromTime.toEpochSecond(ZoneOffset.ofHours(0))*1000
        val toTimeEpochMilli = toTime.toEpochSecond(ZoneOffset.ofHours(0))*1000

        val filteredValues = dummyData.filter { x -> x.timestamp in fromTimeEpochMilli until toTimeEpochMilli }
        inRangeValues.value = filteredValues
    }

    //////////////////////////////////////////////////////////////////
    // Create Dummy data values
    //////////////////////////////////////////////////////////////////


    private fun createDummyData(): List<WeatherDataPoint> {
        val dummyData: MutableList<WeatherDataPoint> = mutableListOf()

        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-07T20:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-07T21:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-07T22:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-07T23:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T00:00:00"), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T01:00:00"), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T02:00:00"), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T03:00:00"), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T04:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T05:00:00"), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T06:00:00"), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T07:00:00"), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T08:00:00"), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T09:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T10:00:00"), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T11:00:00"), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T12:00:00"), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T13:00:00"), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T14:00:00"), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint(getEpicMilliseconds("2021-08-08T15:00:00"), 18.5f, 18f, 82f, 55f))

        return dummyData
    }

    private fun getEpicMilliseconds(dateString: String): Long{
        return LocalDateTime.parse(dateString).atOffset(ZoneOffset.ofHours(0)).toInstant().toEpochMilli()
    }








}
