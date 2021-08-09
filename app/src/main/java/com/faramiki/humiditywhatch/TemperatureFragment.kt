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
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


private const val FACTOR_DAYS_TO_MILLISEC = 86400000

class TemperatureFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var chartTemperature: LineChart
    private lateinit var tvTimeStamp: TextView
    private lateinit var tvValueIn: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.temperature_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        tvTimeStamp = view.findViewById(R.id.tv_timestamp)
        tvValueIn = view.findViewById(R.id.tv_value_in)

        chartTemperature = view.findViewById(R.id.chart_temp)
        chartTemperature.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        chartTemperature.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartTemperature.xAxis.textSize = 12f
        chartTemperature.xAxis.isGranularityEnabled = true
        chartTemperature.xAxis.granularity = 24f


        //chartTemperature.setXAxisRenderer(CustomXAxisRenderer(chartTemperature.viewPortHandler, chartTemperature.xAxis, chartTemperature.getTransformer( YAxis.AxisDependency.LEFT)))


        chartTemperature.axisLeft.axisMinimum = 0f
        chartTemperature.axisLeft.axisMaximum = 40f
        chartTemperature.axisRight.isEnabled = false

        chartTemperature.setExtraOffsets(5f,5f,5f,20f)
        chartTemperature.legend.yOffset = 30f
        chartTemperature.description.isEnabled = false

        chartTemperature.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight?) {

                tvTimeStamp.text = e.x.toLong().toDateTimeStrFromEpochHours() + " Uhr:"
                tvValueIn.text = e.y.toString() + "°C"
            }

            override fun onNothingSelected() {
                tvTimeStamp.text = ""
                tvValueIn.text = ""
            }
        })


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
        lineDataSetTemp.setDrawHorizontalHighlightIndicator(false)
        //to make the smooth line as the graph is adrapt change so smooth curve
        lineDataSetTemp.mode = LineDataSet.Mode.CUBIC_BEZIER
        //to enable the cubic density : if 1 then it will be sharp curve
        lineDataSetTemp.cubicIntensity = 0.2f
        lineDataSetTemp.setDrawValues(false)

        val lines = mutableListOf<ILineDataSet>(lineDataSetTemp)
        chartTemperature.data = LineData(lines)

        chartTemperature.invalidate()
    }




    private fun getTempValues(dataPoints: List<WeatherDataPoint>): List<Entry> {
        return dataPoints.map { x -> Entry(x.timestamp.toFloat(), x.tempIn!!) }
    }


}

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toLong().toDateStrFromEpochHours()
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