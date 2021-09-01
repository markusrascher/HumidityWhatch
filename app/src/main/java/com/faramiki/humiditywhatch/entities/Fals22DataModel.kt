package com.faramiki.humiditywhatch.entities

import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import java.time.LocalDate

class Fals22DataModel {
    var t: Int = 0
    var temp_in: Float = 0f
    var temp_out: Float = 0f
    var hum_in: Float = 0f
    var hum_out: Float = 0f

    fun toWeatherDataPoint(date: LocalDate): WeatherDataPoint{
        return WeatherDataPoint(
            date.toEpochHours() + t,
            temp_in,
            temp_out,
            hum_in,
            hum_out
        )
    }



}