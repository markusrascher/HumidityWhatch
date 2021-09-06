package com.faramiki.humiditywhatch.ui

import com.faramiki.humiditywhatch.MainViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class ChartValueSelectedListener(private var viewModel: MainViewModel): OnChartValueSelectedListener {

    override fun onValueSelected(e: Entry, h: Highlight?) {
        viewModel.setSelectedValue(e.x.toLong())
    }

    override fun onNothingSelected() {
        viewModel.setSelectedValue(null)
    }

}