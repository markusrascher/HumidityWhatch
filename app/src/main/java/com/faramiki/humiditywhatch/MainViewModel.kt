package com.faramiki.humiditywhatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


private const val FACTOR_DAYS_TO_MILLISEC = 86400000L

class MainViewModel : ViewModel()
{
    //Date range
    private var rangeFromEpochHours: MutableLiveData<Long> = MutableLiveData()
    private var rangeToEpochHours:  MutableLiveData<Long> = MutableLiveData()

    //Weather data points in range
    private var inRangeValues: MutableLiveData<List<WeatherDataPoint>> = MutableLiveData()

    // Dummy data points
    private var dummyData: List<WeatherDataPoint> = createDummyData()

    init {
        rangeFromEpochHours.value = LocalDate.now().toEpochHours()
        rangeToEpochHours.value = LocalDate.now().toEpochHours()
        setInRangeValues()
    }

    ///////////////////////////////////
    // Getters / Setters for date range
    ///////////////////////////////////

    fun setRangeFromEpochHours(epochHours: Long)
    {
        this.rangeFromEpochHours.value = epochHours
        setInRangeValues()
    }

    fun setRangeToEpochHours(epochHours: Long)
    {
        this.rangeToEpochHours.value = epochHours
        setInRangeValues()
    }

    fun getRangeFromEpochHours(): LiveData<Long>{
        return rangeFromEpochHours
    }

    fun getRangeToEpochHours(): LiveData<Long>{
        return rangeToEpochHours
    }

    ///////////////////////////////////
    // Getter / Setter for InRange Values
    ///////////////////////////////////

    fun getInRangeValues(): LiveData<List<WeatherDataPoint>>{
        return inRangeValues
    }

    private fun setInRangeValues() {

        val filteredValues = dummyData.filter { x -> x.timestamp in rangeFromEpochHours.value!! until rangeToEpochHours.value!! }
        inRangeValues.value = filteredValues
    }

    fun getValue(selectedEpochHour: Long): WeatherDataPoint {
        return inRangeValues.value!!.find { x -> x.timestamp == selectedEpochHour }!!
    }

    //////////////////////////////////////////////////////////////////
    // Create Dummy data values
    //////////////////////////////////////////////////////////////////


    private fun createDummyData(): List<WeatherDataPoint> {
        val dummyData: MutableList<WeatherDataPoint> = mutableListOf()

        dummyData.add(WeatherDataPoint("2021-08-07 20".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-07 21".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-07 22".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-07 23".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 00".toEpochHours(), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 01".toEpochHours(), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 02".toEpochHours(), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 03".toEpochHours(), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 04".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 05".toEpochHours(), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 06".toEpochHours(), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 07".toEpochHours(), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 08".toEpochHours(), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 09".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 10".toEpochHours(), 18.5f, 18f, 82f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 11".toEpochHours(), 20.5f, 14f, 78f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 12".toEpochHours(), 20.8f, 15f, 79f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 13".toEpochHours(), 21.5f, 16f, 80f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 14".toEpochHours(), 19.5f, 17f, 81f, 55f))
        dummyData.add(WeatherDataPoint("2021-08-08 15".toEpochHours(), 18.5f, 18f, 82f, 55f))

        return dummyData
    }


}
