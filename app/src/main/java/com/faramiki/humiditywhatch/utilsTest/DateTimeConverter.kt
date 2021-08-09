package com.faramiki.humiditywhatch.utilsTest

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val FACTOR_HOURS_SEC = 3600

fun String.toEpochHours(): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH")
    val dateTime = LocalDateTime.parse(this, formatter)
    return dateTime.toEpochHours()
}

fun Long.toDateTimeStrFromEpochHours(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH")

    val dateTime = this.toLocalDateTimeFromEpochHours()
    return dateTime.format(formatter)
}

fun Long.toDateStrFromEpochHours(): String {
    val date = LocalDate.ofEpochDay(this / 24)
    return date.toString()
}


fun Long.toLocalDateTimeFromEpochHours(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(
        this * FACTOR_HOURS_SEC,
        0, ZoneOffset.ofHours(0)
    )
}

fun LocalDateTime.toEpochHours(): Long {
    val epochSeconds = this.toEpochSecond(ZoneOffset.ofHours(0))

    return epochSeconds / FACTOR_HOURS_SEC
}

fun LocalDate.toEpochHours(): Long {

    return toEpochDay() * 24
}