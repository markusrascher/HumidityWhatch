package com.faramiki.humiditywhatch.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.faramiki.humiditywhatch.R
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.utilsTest.toDateTimeStrFromEpochHours
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class HumRelFragment: WeatherDataBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chart_fragment, container)
    }


    override fun setChartData() {
        val epochHoursFrom = mainViewModel.getRangeFromEpochHours().value!!
        val epochHoursTo = mainViewModel.getRangeToEpochHours().value!!
        val allLoadedDataPoints: List<WeatherDataPoint> = mainViewModel.getWeatherData().value!!

        val shownDataPoints = allLoadedDataPoints.filter { it.timestamp in epochHoursFrom until epochHoursTo }

        val dataPointsTempIn = shownDataPoints.map { x -> Entry(x.timestamp.toFloat(), x.humRIn!!)}
        val lineDataSetTempIn = LineDataSet(dataPointsTempIn, getString(R.string.Hum_rel_In))
        formatLineChart(lineDataSetTempIn, Color.RED)

        val dataPointsTempOut = shownDataPoints.map { x -> Entry(x.timestamp.toFloat(), x.humROut!!)}
        val lineDataSetTempOut = LineDataSet(dataPointsTempOut, getString(R.string.Hum_rel_Out))
        formatLineChart(lineDataSetTempOut, Color.BLUE)

        val lines = mutableListOf<ILineDataSet>(lineDataSetTempIn, lineDataSetTempOut)
        chartView.data = LineData(lines)
        chartView.invalidate()
    }

    override fun updateCurrentValue(newValue: WeatherDataPoint?) {

        if (newValue != null)
        {
            tvCurTimestamp.text = getString(R.string.DateTimeValue, newValue.timestamp.toDateTimeStrFromEpochHours())
            tvCurValIn.text = getString(R.string.HumRelValue, newValue.humRIn.toString())
            tvCurValOut.text = getString(R.string.HumRelValue, newValue.humROut.toString())
        }
        else {
            tvCurTimestamp.text = getString(R.string.NoValueSelected)
            tvCurValIn.text = "---"
            tvCurValOut.text = "---"
        }
    }

    override fun setChartYAxis() {
        chartView.axisLeft.axisMinimum = 40f
        chartView.axisLeft.axisMaximum = 100f

        chartView.axisRight.axisMinimum = 40f
        chartView.axisRight.axisMaximum = 100f
    }
}