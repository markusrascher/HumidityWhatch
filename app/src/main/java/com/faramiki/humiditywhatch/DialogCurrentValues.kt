package com.faramiki.humiditywhatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.utilsTest.toDateTimeStrFromEpochHours

class DialogCurrentValues : DialogFragment() {

    private lateinit var dialogView: View
    private lateinit var mainViewModel: MainViewModel

    private lateinit var tvTimestamp: TextView
    private lateinit var tvCurValIn: TextView
    private lateinit var tvCurValOut: TextView

    private lateinit var currentDataPoint: WeatherDataPoint
    private var activeTab = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogView = inflater.inflate(R.layout.dialog_current_values, container, false)
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        tvTimestamp = view.findViewById(R.id.tv_timestamp)
        tvCurValIn = view.findViewById(R.id.tv_value_in)
        tvCurValOut = view.findViewById(R.id.tv_value_out)

        if (activeTab == 1){
            tvTimestamp.text = getString(R.string.DateTimeValue, currentDataPoint.timestamp.toDateTimeStrFromEpochHours())
            tvCurValIn.text = getString(R.string.TempValue, currentDataPoint.tempIn.toString())
            tvCurValOut.text = getString(R.string.TempValue, currentDataPoint.tempOut.toString())
        }
    }

    fun updateValues(weatherDataPoint: WeatherDataPoint, activeTab: Int){
        this.currentDataPoint = weatherDataPoint
        this.activeTab = activeTab
    }

}