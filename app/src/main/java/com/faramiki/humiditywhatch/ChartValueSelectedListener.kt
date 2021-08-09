package com.faramiki.humiditywhatch

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class ChartValueSelectedListener(
    private var viewModel: MainViewModel,
    private var activeTab: Int,
    private var fragmentManager: FragmentManager): OnChartValueSelectedListener {

    private var currentValuesDialog = DialogCurrentValues()


    override fun onValueSelected(e: Entry, h: Highlight?) {

        val selectedEpochHour = e.x.toLong()
        val dataPoint = viewModel.getValue(selectedEpochHour)
        
        currentValuesDialog.show(fragmentManager, "Current values")
        currentValuesDialog.updateValues(dataPoint, activeTab)

    }

    override fun onNothingSelected() {

    }

}