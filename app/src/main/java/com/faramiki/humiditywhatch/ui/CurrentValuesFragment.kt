package com.faramiki.humiditywhatch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.MainViewModel
import com.faramiki.humiditywhatch.R
import com.faramiki.humiditywhatch.entities.WeatherDataPoint
import com.faramiki.humiditywhatch.utilsTest.toDateTimeStrFromEpochHours

class CurrentValuesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var tvTimestamp: TextView
    private lateinit var tvCurValIn: TextView
    private lateinit var tvCurValOut: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.current_values_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        tvTimestamp = view.findViewById(R.id.tv_timestamp)
        tvCurValIn = view.findViewById(R.id.tv_value_in)
        tvCurValOut = view.findViewById(R.id.tv_value_out)

        mainViewModel.getSelectedValue().observe(viewLifecycleOwner, { newValue ->
            updateValues(newValue) })
    }

    private fun updateValues(weatherDataPoint: WeatherDataPoint?){

        if (weatherDataPoint != null)
        {
            tvTimestamp.text = getString(R.string.DateTimeValue, weatherDataPoint.timestamp.toDateTimeStrFromEpochHours())
            tvCurValIn.text = getString(R.string.TempValue, weatherDataPoint.tempIn.toString())
            tvCurValOut.text = getString(R.string.TempValue, weatherDataPoint.tempOut.toString())
        }

    }

}