package com.faramiki.humiditywhatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.repositories.WeatherDataRepository
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import com.faramiki.humiditywhatch.utilsTest.toLocalDateFromEpochHours
import java.time.LocalDate


class MainViewModel : ViewModel()
{
    private var weatherDataRepository: WeatherDataRepository? = null

    private var weatherData: LiveData<List<WeatherDataPoint>>
    private var rangeFromEpochHours: MutableLiveData<Long> = MutableLiveData()
    private var rangeToEpochHours:  MutableLiveData<Long> = MutableLiveData()
    private var selectedValue: MutableLiveData<WeatherDataPoint?> = MutableLiveData()
    var weatherDataRepoStatus: LiveData<Boolean>

    init {
        weatherDataRepository = WeatherDataRepository()
        weatherDataRepoStatus = weatherDataRepository!!.getStatus()

        weatherData = weatherDataRepository!!.getLoadedValues()

        rangeFromEpochHours.value = LocalDate.now().toEpochHours()
        rangeToEpochHours.value = LocalDate.now().toEpochHours() + 23

        weatherDataRepository!!.getWeatherData(LocalDate.now(), LocalDate.now())
    }


    ///////////////////////////////////
    // Getters / Setters
    ///////////////////////////////////

    fun getWeatherData(): LiveData<List<WeatherDataPoint>>{
        return weatherData
    }

    fun setRangeFromEpochHours(epochHours: Long)
    {
        rangeFromEpochHours.value = epochHours

        weatherDataRepository!!.getWeatherData(
            rangeFromEpochHours.value!!.toLocalDateFromEpochHours(),
            rangeToEpochHours.value!!.toLocalDateFromEpochHours()
        )
    }

    fun setRangeToEpochHours(epochHours: Long)
    {
        this.rangeToEpochHours.value = epochHours

        weatherDataRepository!!.getWeatherData(
            rangeFromEpochHours.value!!.toLocalDateFromEpochHours(),
            rangeToEpochHours.value!!.toLocalDateFromEpochHours()
        )
    }

    fun getRangeFromEpochHours(): LiveData<Long>{
        return rangeFromEpochHours
    }

    fun getRangeToEpochHours(): LiveData<Long>{
        return rangeToEpochHours
    }

    fun setSelectedValue(selectedEpochHour: Long?){
        if(selectedEpochHour != null){
            selectedValue.value = weatherData.value!!.find { x -> x.timestamp == selectedEpochHour }!!
        }
        else{
            selectedValue.value = null
        }
    }

    fun getSelectedValue(): LiveData<WeatherDataPoint?>{
        return selectedValue
    }
}
