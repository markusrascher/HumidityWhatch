package com.faramiki.humiditywhatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.faramiki.humiditywhatch.ui.TabAdapter
import com.faramiki.humiditywhatch.ui.TabConfigurationStrategy
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.weatherDataRepoStatus.observe(this, { newValue -> showRepoStatus(newValue) })

        initViews()
    }

    private fun showRepoStatus(newValue: Boolean) {
        if(!newValue)
        {
            Toast.makeText(this, "Could not load values!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = TabAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager, TabConfigurationStrategy())
        tabLayoutMediator.attach()
    }

}