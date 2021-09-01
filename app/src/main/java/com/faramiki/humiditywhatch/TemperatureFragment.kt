package com.faramiki.humiditywhatch

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.utilsTest.toDateStrFromEpochHours
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class TemperatureFragment: Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var chartTemperature: LineChart
    private lateinit var currentValuesFragment: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.temperature_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        currentValuesFragment = view.findViewById(R.id.current_values_fragment)
        chartTemperature = view.findViewById(R.id.chart_temp)

        initChart()
        mainViewModel.getWeatherData().observe(viewLifecycleOwner, { setChartData() })
        mainViewModel.getSelectedValue().observe(viewLifecycleOwner, { newValue -> setCurrentValuesFragmentVisibility(newValue) })
        mainViewModel.setSelectedValue(null)


        // Add Observers for the dates
        mainViewModel.getRangeFromEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateFrom(newValue) })

        mainViewModel.getRangeToEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateTo(newValue) })

    }

    private fun setCurrentValuesFragmentVisibility(newValue: WeatherDataPoint?) {
        if ( newValue != null)
        {
            currentValuesFragment.visibility = View.VISIBLE
        }
        else{
            currentValuesFragment.visibility = View.INVISIBLE
        }
    }

    private fun initChart() {

        chartTemperature.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        chartTemperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartTemperature.xAxis.textSize = 12f
        chartTemperature.xAxis.isGranularityEnabled = true
        chartTemperature.xAxis.granularity = 24f

        chartTemperature.axisLeft.axisMinimum = 0f
        chartTemperature.axisLeft.axisMaximum = 40f

        chartTemperature.axisRight.axisMinimum = 0f
        chartTemperature.axisRight.axisMaximum = 40f

        chartTemperature.setExtraOffsets(5f,5f,5f,20f)
        chartTemperature.legend.yOffset = 30f
        chartTemperature.description.isEnabled = false

        chartTemperature.setOnChartValueSelectedListener(ChartValueSelectedListener(mainViewModel))
    }

    private fun setChartData() {
        val epochHoursFrom = mainViewModel.getRangeFromEpochHours().value!!
        val epochHoursTo = mainViewModel.getRangeToEpochHours().value!!
        val allLoadedDataPoints: List<WeatherDataPoint> = mainViewModel.getWeatherData().value!!

        val shownDataPoints = allLoadedDataPoints.filter { it.timestamp in epochHoursFrom until epochHoursTo }

        val dataPointsTempIn = shownDataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempIn!!)}
        val lineDataSetTempIn = LineDataSet(dataPointsTempIn, getString(R.string.Temp_In))
        formatLineChart(lineDataSetTempIn, Color.RED)

        val dataPointsTempOut = shownDataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempOut!!)}
        val lineDataSetTempOut = LineDataSet(dataPointsTempOut, getString(R.string.Temp_Out))
        formatLineChart(lineDataSetTempOut, Color.BLUE)

        val lines = mutableListOf<ILineDataSet>(lineDataSetTempIn, lineDataSetTempOut)
        chartTemperature.data = LineData(lines)
        chartTemperature.invalidate()

    }

    private fun formatLineChart(lineDataSet: LineDataSet, color: Int){

        lineDataSet.color = color
        lineDataSet.fillColor = color
        lineDataSet.setCircleColor(color)
        lineDataSet.valueTextSize = 12f
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.setDrawHorizontalHighlightIndicator(false)

        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.2f
        lineDataSet.setDrawValues(false)
    }

    private fun updateDateTo(newValue: Long?) {
        if (newValue != null)
        {
            chartTemperature.xAxis.axisMaximum = newValue.toFloat()
            setChartData()
        }
    }

    private fun updateDateFrom(newValue: Long?) {
        if (newValue != null)
        {
            chartTemperature.xAxis.axisMinimum = newValue.toFloat()
            setChartData()
        }
    }
}

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toLong().toDateStrFromEpochHours()
    }
}