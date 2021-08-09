package com.faramiki.humiditywhatch

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.utilsTest.toDateStrFromEpochHours
import com.faramiki.humiditywhatch.utilsTest.toDateTimeStrFromEpochHours
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class TemperatureFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var chartTemperature: LineChart


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.temperature_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        chartTemperature = view.findViewById(R.id.chart_temp)

        initChart()
        setChartData()

        viewModel.getInRangeValues().observe(viewLifecycleOwner, { setChartData() })
    }

    private fun initChart() {

        chartTemperature.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        chartTemperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartTemperature.xAxis.textSize = 12f
        chartTemperature.xAxis.isGranularityEnabled = true
        chartTemperature.xAxis.granularity = 24f

        chartTemperature.axisLeft.axisMinimum = 0f
        chartTemperature.axisLeft.axisMaximum = 40f

        chartTemperature.axisRight.isEnabled = false

        chartTemperature.setExtraOffsets(5f,5f,5f,20f)
        chartTemperature.legend.yOffset = 30f
        chartTemperature.description.isEnabled = false

        chartTemperature.setOnChartValueSelectedListener(ChartValueSelectedListener(viewModel, 1, childFragmentManager))
    }

    private fun setChartData() {
        val dataPoints = viewModel.getInRangeValues().value!!

        val dataPointsTempIn = dataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempIn!!)}
        val lineDataSetTempIn = LineDataSet(dataPointsTempIn, getString(R.string.Temp_In))
        formatLineChart(lineDataSetTempIn, Color.RED)

        val dataPointsTempOut = dataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempOut!!)}
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

}

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toLong().toDateStrFromEpochHours()
    }
}