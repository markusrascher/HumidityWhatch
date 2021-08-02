package com.faramiki.humiditywhatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPager!!.adapter = TabAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(tabLayout!!, viewPager!!, TabConfigurationStrategy())
        tabLayoutMediator.attach()
    }
}