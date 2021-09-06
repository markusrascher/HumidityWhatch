package com.faramiki.humiditywhatch.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.MainViewModel
import com.faramiki.humiditywhatch.R
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet


abstract class WeatherDataBaseFragment: Fragment() {
    protected lateinit var mainViewModel: MainViewModel
    protected lateinit var chartView: LineChart

    protected lateinit var tvCurTimestamp: TextView
    protected lateinit var tvCurValIn: TextView
    protected lateinit var tvCurValOut: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        chartView = view.findViewById(R.id.chart)
        tvCurTimestamp = view.findViewById(R.id.tv_timestamp)
        tvCurValIn = view.findViewById(R.id.tv_value_in)
        tvCurValOut = view.findViewById(R.id.tv_value_out)

        initChart()
        mainViewModel.getWeatherData().observe(viewLifecycleOwner, { setChartData() })

        // Add Observers for the dates
        mainViewModel.getRangeFromEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateFrom(newValue) })

        mainViewModel.getRangeToEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateTo(newValue) })

        // Observer for Current Values
        mainViewModel.getSelectedValue().observe(viewLifecycleOwner, { newValue ->
            updateCurrentValue(newValue) })

    }

    private fun initChart() {

        chartView.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        chartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartView.xAxis.textSize = 12f
        chartView.xAxis.isGranularityEnabled = true
        chartView.xAxis.granularity = 24f

        chartView.legend.isEnabled = false

        setChartYAxis()

        chartView.setExtraOffsets(5f,5f,5f,5f)
        chartView.description.isEnabled = false

        chartView.setOnChartValueSelectedListener(ChartValueSelectedListener(mainViewModel))
    }


    private fun updateDateTo(newValue: Long?) {
        if (newValue != null)
        {
            chartView.xAxis.axisMaximum = newValue.toFloat()
            setChartData()
        }
    }

    private fun updateDateFrom(newValue: Long?) {
        if (newValue != null)
        {
            chartView.xAxis.axisMinimum = newValue.toFloat()
            setChartData()
        }
    }

    protected fun formatLineChart(lineDataSet: LineDataSet, color: Int){

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

    protected abstract fun setChartYAxis()

    protected abstract fun setChartData()

    protected abstract fun updateCurrentValue(newValue: WeatherDataPoint?)

}