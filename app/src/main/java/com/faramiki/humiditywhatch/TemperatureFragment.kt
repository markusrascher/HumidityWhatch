package com.faramiki.humiditywhatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class TemperatureFragment: Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.temperature_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView: TextView = view.findViewById(R.id.text)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.getValue().observe(viewLifecycleOwner, { value -> textView.text = value.tempIn.toString() })
    }
}