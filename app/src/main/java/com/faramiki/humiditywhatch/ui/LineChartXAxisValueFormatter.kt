package com.faramiki.humiditywhatch.ui

import com.faramiki.humiditywhatch.utilsTest.toDateStrFromEpochHours
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toLong().toDateStrFromEpochHours()
    }
}