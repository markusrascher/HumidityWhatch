package com.faramiki.humiditywhatch

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy {
    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        when (position) {
            0 -> tab.setText(R.string.temperature)
            1 -> tab.setText(R.string.hum_rel)
        }
    }
}