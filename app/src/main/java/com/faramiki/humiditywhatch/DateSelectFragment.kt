package com.faramiki.humiditywhatch

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faramiki.humiditywhatch.utilsTest.toDateStrFromEpochHours
import com.faramiki.humiditywhatch.utilsTest.toDateTimeStrFromEpochHours
import com.faramiki.humiditywhatch.utilsTest.toEpochHours
import com.faramiki.humiditywhatch.utilsTest.toLocalDateTimeFromEpochHours
import java.time.LocalDate
import java.util.*

private const val FACTOR_DAYS_TO_MILLISEC = 86400000
private const val FACTOR_HOURS_TO_MILLISEC = 3600000


class DateSelectFragment: Fragment() {

    private lateinit var mainViewModel: MainViewModel

    // Buttons:
    private lateinit var btnSelectDateFrom: ImageView
    private lateinit var btnSelectDateTo: ImageView
    private lateinit var btnEarlier: ImageView
    private lateinit var btnLater: ImageView

    // Date selectors
    private lateinit var tvDateFrom: TextView
    private lateinit var tvDateTo: TextView
    private lateinit var datePicker: DatePickerDialog

    // Dates
    private lateinit var dpFrom: DatePickerDialog
    private lateinit var dpTo: DatePickerDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.date_select_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        //Views and actions
        btnSelectDateFrom = view.findViewById(R.id.iv_select_from)
        btnSelectDateFrom.setOnClickListener { dpFrom.show() }

        btnSelectDateTo = view.findViewById(R.id.iv_select_to)
        btnSelectDateTo.setOnClickListener { dpTo.show() }

        btnEarlier = view.findViewById(R.id.iv_earlier)
        btnEarlier.setOnClickListener { reduceFromDateByOneDay() }

        btnLater = view.findViewById(R.id.iv_later)
        btnLater.setOnClickListener { increaseToDateByOneDay() }

        tvDateFrom = view.findViewById(R.id.tv_date_from_value)
        tvDateFrom.setOnClickListener { dpFrom.show() }

        tvDateTo = view.findViewById(R.id.tv_date_to_value)
        tvDateTo.setOnClickListener { dpTo.show() }

        // Add Observers for the dates
        mainViewModel.getRangeFromEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateFrom(newValue) })

        mainViewModel.getRangeToEpochHours().observe(viewLifecycleOwner, { newValue ->
            updateDateTo(newValue) })

        // Datepicker
        initDatePickers()
    }

    private fun initDatePickers() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dpFrom =  DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateFrom(yy, mm, dd)},
            year,
            month,
            day
        )

        dpTo =  DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateTo(yy, mm, dd)},
            year,
            month,
            day
        )

        dpTo.datePicker.maxDate = LocalDate.now().toEpochDay() * FACTOR_DAYS_TO_MILLISEC
    }

    private fun updateDateFrom(newValue: Long) {
        tvDateFrom.text = newValue.toDateStrFromEpochHours()
        setViewDependencies()
    }

    private fun updateDateTo(newValue: Long) {
        tvDateTo.text = newValue.toDateStrFromEpochHours()
        setViewDependencies()
    }

    private fun setViewDependencies() {

        val todayEpochHours = LocalDate.now().toEpochHours()
        val curRangeFromEpochHours = mainViewModel.getRangeFromEpochHours().value!!
        val curRangeToEpochHours = mainViewModel.getRangeToEpochHours().value!!

        dpFrom.datePicker.maxDate = curRangeToEpochHours * FACTOR_HOURS_TO_MILLISEC
        dpTo.datePicker.minDate = curRangeFromEpochHours * FACTOR_HOURS_TO_MILLISEC

        //TODO: update date pickers current values if date was set by earlier / later
        btnLater.isEnabled  = curRangeToEpochHours != todayEpochHours
    }

    private fun setDateFrom(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth) + " 00"

        mainViewModel.setRangeFromEpochHours(dateString.toEpochHours())
    }

    private fun setDateTo(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth) + " 00"

        mainViewModel.setRangeToEpochHours(dateString.toEpochHours())
        setViewDependencies()
    }

    private fun reduceFromDateByOneDay(){
        mainViewModel.setRangeFromEpochHours(mainViewModel.getRangeFromEpochHours().value!! - 24)
    }

    private fun increaseToDateByOneDay(){
        mainViewModel.setRangeToEpochHours(mainViewModel.getRangeToEpochHours().value!! + 24)
    }
}