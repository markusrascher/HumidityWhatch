package com.faramiki.humiditywhatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.temperature)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.hum_rel)))


    }
}