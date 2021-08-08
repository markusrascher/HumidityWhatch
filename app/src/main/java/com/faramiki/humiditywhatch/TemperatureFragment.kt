package com.faramiki.humiditywhatch

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.SimpleDateFormat
import java.util.*

private const val FACTOR_DAYS_TO_MILLISEC = 86400000

class TemperatureFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var chartTemperature: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.temperature_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val textView: TextView = view.findViewById(R.id.text)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        chartTemperature = view.findViewById(R.id.chart_temp)
        chartTemperature.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        chartTemperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartTemperature.xAxis.textSize = 12f
        chartTemperature.xAxis.isGranularityEnabled = true
        chartTemperature.xAxis.granularity = 3600000f


        chartTemperature.setXAxisRenderer(CustomXAxisRenderer(chartTemperature.viewPortHandler, chartTemperature.xAxis, chartTemperature.getTransformer( YAxis.AxisDependency.LEFT)))


        chartTemperature.axisLeft.axisMinimum = 0f
        chartTemperature.axisLeft.axisMaximum = 40f


        chartTemperature.setExtraOffsets(5f,5f,5f,20f)
        chartTemperature.legend.yOffset = 30f
        chartTemperature.description.isEnabled = false

        setChartData()



        viewModel.getInRangeValues().observe(viewLifecycleOwner, { setChartData()})
    }

    private fun setChartData() {

        val dataPoints = getTempValues(viewModel.getInRangeValues().value!!)
        val lineDataSetTemp = LineDataSet(dataPoints, "Temperature")
        lineDataSetTemp.color = Color.RED
        lineDataSetTemp.fillColor = Color.RED
        lineDataSetTemp.setCircleColor(Color.RED)
        lineDataSetTemp.valueTextSize = 12f
        lineDataSetTemp.axisDependency = YAxis.AxisDependency.LEFT
        //lineDataSetTemp.setDrawValues(false)

        val lines = mutableListOf<ILineDataSet>(lineDataSetTemp)
        chartTemperature.data = LineData(lines)
        //chartTemperature.xAxis.labelCount = 6
        chartTemperature.xAxis.axisMinimum = (viewModel.getDateFromEpochDays().value!! * FACTOR_DAYS_TO_MILLISEC).toFloat()

        chartTemperature.invalidate()
    }




    private fun getTempValues(dataPoints: List<WeatherDataPoint>): List<Entry> {
        return dataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempIn!!) }
    }


}

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        // Convert float value to date string
        // Convert from seconds back to milliseconds to format time  to show to the user
        val emissionsMilliSince1970Time = value.toLong()
        val date = Date(emissionsMilliSince1970Time)
        val format = SimpleDateFormat("dd.MM\nHH")
        return format.format(date) + " Uhr"
    }
}


class CustomXAxisRenderer(viewPortHandler: ViewPortHandler?, xAxis: XAxis?, trans: Transformer?) :
    XAxisRenderer(viewPortHandler, xAxis, trans) {
    override fun drawLabel(
        c: Canvas?,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        val line = formattedLabel.split("\n").toTypedArray()
        Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees)
        Utils.drawXAxisValue(c, line[1], x, y + mAxisLabelPaint.textSize, mAxisLabelPaint, anchor, angleDegrees )
    }
}