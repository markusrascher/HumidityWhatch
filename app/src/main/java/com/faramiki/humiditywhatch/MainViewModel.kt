package com.faramiki.humiditywhatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.repositories.WeatherDataRepository
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import java.time.LocalDate


class MainViewModel : ViewModel()
{
    //Date range
    private var rangeFromEpochHours: MutableLiveData<Long> = MutableLiveData()
    private var rangeToEpochHours:  MutableLiveData<Long> = MutableLiveData()

    //Weather data points in range
    private var inRangeValues: MutableLiveData<List<WeatherDataPoint>> = MutableLiveData()
    private var selectedValue: MutableLiveData<WeatherDataPoint?> = MutableLiveData()

    //Repository
    private var weatherDataRepository: WeatherDataRepository? = null
    private var weatherData: LiveData<List<WeatherDataPoint>?>? = null




    // Dummy data points




    //private var dummyData: List<WeatherDataPoint> = createDummyData()
    //private var dummyData: List<WeatherDataPoint>()




    init {
        weatherDataRepository = WeatherDataRepository()
        weatherData = MutableLiveData()
        fetchData()

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

        val filteredValues = mutableListOf<WeatherDataPoint>()
        if (weatherData?.value != null){
            filteredValues.addAll(weatherData?.value!!.filter { x -> x.timestamp in rangeFromEpochHours.value!! until rangeToEpochHours.value!! + 24 })
        }

        inRangeValues.value = filteredValues
    }


    fun setSelectedValue(selectedEpochHour: Long?){
        if(selectedEpochHour != null){
            selectedValue.value = inRangeValues.value!!.find { x -> x.timestamp == selectedEpochHour }!!
        }
        else{
            selectedValue.value = null
        }
    }

    fun getSelectedValue(): LiveData<WeatherDataPoint?>{
        return selectedValue
    }


    fun fetchData(){
        weatherData = weatherDataRepository?.getOnlineWeatherData("2021-08-26 00".toEpochHours())
    }


}
