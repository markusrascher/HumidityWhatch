package com.faramiki.humiditywhatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class HumRelFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hum_rel_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView: TextView = view.findViewById(R.id.text)
        textView.setText(R.string.hum_rel)
    }

}