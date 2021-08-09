package com.faramiki.humiditywhatch.utilsTest

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverterTest {
    @Test
    fun convertStrToEpochHoursPositive() {

        val input = "2021-08-07 20"
        val expectedResult = 452324L
        val result = input.toEpochHours()

        assertEquals(expectedResult, result)
    }


    @Test
    fun convertEpochHoursToStrPositive() {

        val input = 452324L
        val expectedResult = "2021-08-07 20"

        val result = input.toDateTimeStrFromEpochHours()

        assertEquals(expectedResult, result)
    }


    @Test
    fun convertEpochHoursToLocalDateTimePositive() {

        val inputStr = "2021-08-07 20"
        val inoutEpochHours = 452324L

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH")
        val expectedResult = LocalDateTime.parse(inputStr, formatter)

        val result = inoutEpochHours.toLocalDateTimeFromEpochHours()

        assertEquals(expectedResult, result)
    }

    @Test
    fun convertEpochHoursToLocalDatePositive() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH")

        val inputStr = "2021-08-07 00"
        val inoutEpochHours = 452304L

        val expectedResult = LocalDate.parse(inputStr, formatter).toString()


        val result = inoutEpochHours.toDateStrFromEpochHours()

        assertEquals(expectedResult, result)
    }


}