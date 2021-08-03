package com.faramiki.humiditywhatch.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_weather_data")
data class WeatherDataPoint (

    @PrimaryKey val timestamp: Long,

    @ColumnInfo(name = "temp_in") val tempIn: Float?,

    @ColumnInfo(name = "temp_out") val tempOut: Float?,

    @ColumnInfo(name = "hum_r_in") val humRIn: Float?,

    @ColumnInfo(name = "hum_r_out") val humROut: Float?,

)
