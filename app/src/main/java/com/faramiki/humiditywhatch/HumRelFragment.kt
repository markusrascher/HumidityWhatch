package com.faramiki.humiditywhatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HumRelFragment: Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var textView: TextView
    private lateinit var valuesTable: TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hum_rel_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = view.findViewById(R.id.text)
        //valuesTable = view.findViewById(R.id.tbl_values)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


       // viewModel.getValue().observe(viewLifecycleOwner, { value -> textView.text = value.humRIn.toString() })


    }



}