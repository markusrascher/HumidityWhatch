package com.faramiki.humiditywhatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.faramiki.humiditywhatch.main.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
    var btnSet: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPager!!.adapter = TabAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(tabLayout!!, viewPager!!, TabConfigurationStrategy())
        tabLayoutMediator.attach()

        btnSet = findViewById(R.id.btn_set)
        btnSet!!.setOnClickListener { viewModel.selectValue() }
    }
}