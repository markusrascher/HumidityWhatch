package com.faramiki.humiditywhatch

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.util.*

private const val FACTOR_DAYS_TO_MILLISEC = 86400000

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
        mainViewModel.getDateFromEpochDays().observe(viewLifecycleOwner, { newValue ->
            updateDateFrom(newValue) })

        mainViewModel.getDateToEpochDays().observe(viewLifecycleOwner, { newValue ->
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
        tvDateFrom.text = LocalDate.ofEpochDay(newValue).toString()
        setViewDependencies()
    }

    private fun updateDateTo(newValue: Long) {
        tvDateTo.text = LocalDate.ofEpochDay(newValue).toString()
        setViewDependencies()
    }

    private fun setViewDependencies() {
        val todayEpochDays = LocalDate.now().toEpochDay()
        val curDateFromEpochDays = mainViewModel.getDateFromEpochDays().value!!
        val curDateToEpochDays = mainViewModel.getDateToEpochDays().value!!

        dpFrom.datePicker.maxDate = curDateToEpochDays * FACTOR_DAYS_TO_MILLISEC
        dpTo.datePicker.minDate = curDateFromEpochDays * FACTOR_DAYS_TO_MILLISEC

        //TODO: update date pickers current valeus if date was set by earlier / later
        btnLater.isEnabled  = curDateToEpochDays != todayEpochDays
    }

    private fun setDateFrom(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth)

        mainViewModel.setDateFromEpochDays(LocalDate.parse(dateString).toEpochDay())
    }

    private fun setDateTo(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth)

        mainViewModel.setDateToEpochDays(LocalDate.parse(dateString).toEpochDay())
        setViewDependencies()
    }

    private fun reduceFromDateByOneDay(){
        val curFromDate = LocalDate.ofEpochDay(mainViewModel.getDateFromEpochDays().value!!)
        val newFromDate = curFromDate.plusDays(-1)
        mainViewModel.setDateFromEpochDays(newFromDate.toEpochDay())
    }

    private fun increaseToDateByOneDay(){
        val curToDate = LocalDate.ofEpochDay(mainViewModel.getDateToEpochDays().value!!)
        val newToDate = curToDate.plusDays(1)
        mainViewModel.setDateToEpochDays(newToDate.toEpochDay())
    }
}