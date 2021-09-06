package com.faramiki.humiditywhatch.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.MainViewModel
import com.faramiki.humiditywhatch.R
import com.faramiki.humiditywhatch.utilsTest.toDateStrFromEpochHours
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import java.time.LocalDate

private const val FACTOR_DAYS_TO_MILLISEC = 86400000
private const val FACTOR_HOURS_TO_MILLISEC = 3600000


class DateSelectFragment: Fragment() {

    private lateinit var mainViewModel: MainViewModel

    // Buttons:
    private lateinit var btnFromEarlier: ImageView
    private lateinit var btnFromLater: ImageView

    private lateinit var btnToEarlier: ImageView
    private lateinit var btnToLater: ImageView


    // Date selectors
    private lateinit var tvDateFrom: TextView
    private lateinit var tvDateTo: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.date_select_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        btnFromEarlier = view.findViewById(R.id.iv_from_earlier)
        btnFromEarlier.setOnClickListener { mainViewModel.setRangeFromEpochHours(mainViewModel.getRangeFromEpochHours().value!! - 24) }

        btnFromLater = view.findViewById(R.id.iv_from_later)
        btnFromLater.setOnClickListener { mainViewModel.setRangeFromEpochHours(mainViewModel.getRangeFromEpochHours().value!! + 24) }

        btnToEarlier = view.findViewById(R.id.iv_to_earlier)
        btnToEarlier.setOnClickListener { mainViewModel.setRangeToEpochHours(mainViewModel.getRangeToEpochHours().value!! - 24) }

        btnToLater = view.findViewById(R.id.iv_to_later)
        btnToLater.setOnClickListener { mainViewModel.setRangeToEpochHours(mainViewModel.getRangeToEpochHours().value!! + 24) }

        tvDateFrom = view.findViewById(R.id.tv_date_from_value)
        tvDateFrom.setOnClickListener { showDateFromPicker() }

        tvDateTo = view.findViewById(R.id.tv_date_to_value)
        tvDateTo.setOnClickListener { showDateToPicker() }

        // Add Observers for the dates
        mainViewModel.getRangeFromEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateFrom(newValue) })

        mainViewModel.getRangeToEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateTo(newValue) })
    }


    private fun showDateFromPicker() {
        val date = LocalDate.parse(tvDateFrom.text)

        val dpFrom = DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateFrom(yy, mm, dd)},
            date.year,
            date.monthValue,
            date.dayOfMonth
        )

        val curRangeToEpochHours = mainViewModel.getRangeToEpochHours().value!! - 23
        dpFrom.datePicker.maxDate = curRangeToEpochHours * FACTOR_HOURS_TO_MILLISEC
        dpFrom.show()
    }


    private fun showDateToPicker() {
        val date = LocalDate.parse(tvDateTo.text)

        val dpTo = DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateTo(yy, mm, dd)},
            date.year,
            date.monthValue,
            date.dayOfMonth
        )

        val curRangeFromEpochHours = mainViewModel.getRangeFromEpochHours().value!!

        dpTo.datePicker.maxDate = LocalDate.now().toEpochDay() * FACTOR_DAYS_TO_MILLISEC
        dpTo.datePicker.minDate = curRangeFromEpochHours * FACTOR_HOURS_TO_MILLISEC
        dpTo.show()
    }

    
    ////////////////////////
    // Date Range setters 
    ////////////////////////
    
    private fun setDateFrom(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth) + " 00"

        mainViewModel.setRangeFromEpochHours(dateString.toEpochHours())
    }

    private fun setDateTo(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth) + " 00"

        mainViewModel.setRangeToEpochHours(dateString.toEpochHours() + 23)
    }


    ////////////////////////
    // Date Range observers 
    ////////////////////////
    private fun updateDateFrom(newValue: Long) {
        tvDateFrom.text = newValue.toDateStrFromEpochHours()
        updateViewDependencies()
    }

    private fun updateDateTo(newValue: Long) {
        tvDateTo.text = newValue.toDateStrFromEpochHours()
        updateViewDependencies()
    }

    private fun updateViewDependencies() {

        val todayEpochHours = LocalDate.now().toEpochHours()
        val curRangeFromEpochHours = mainViewModel.getRangeFromEpochHours().value!!
        val curRangeToEpochHours = mainViewModel.getRangeToEpochHours().value!!

        btnFromLater.isEnabled = curRangeFromEpochHours < (curRangeToEpochHours - 23)
        btnToEarlier.isEnabled = btnFromLater.isEnabled
        btnToLater.isEnabled  = curRangeToEpochHours < todayEpochHours
    }
}