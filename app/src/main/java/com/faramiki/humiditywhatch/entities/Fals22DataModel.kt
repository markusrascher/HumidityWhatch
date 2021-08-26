package com.faramiki.humiditywhatch.entities

class Fals22DataModel {
    var t: Int = 0
    var temp_in: Float = 0f
    var temp_out: Float = 0f
    var hum_in: Float = 0f
    var hum_out: Float = 0f

    fun toWeatherDataPoint(dayInEpochHours: Long): WeatherDataPoint{
        return WeatherDataPoint(
            dayInEpochHours + t,
            temp_in,
            temp_out,
            hum_in,
            hum_out
        )
    }



}