package com.faramiki.humiditywhatch.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.faramiki.humiditywhatch.TemperatureFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TemperatureFragment()
            1 -> return HumRelFragment()
        }
        return HumRelFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }

}