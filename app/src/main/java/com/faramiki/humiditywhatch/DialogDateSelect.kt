package com.faramiki.humiditywhatch

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*

private const val FACTOR_DAYS_TO_MILLISEC = 86400000

class DialogDateSelect : DialogFragment() {

    private lateinit var dialogView:View
    private lateinit var mainViewModel: MainViewModel

    // Buttons:
    private lateinit var btnApply: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSelectDateFrom: ImageView
    private lateinit var btnSelectDateTo: ImageView

    // Date selectors
    private lateinit var tvDateFrom: TextView
    private lateinit var tvDateTo: TextView
    private lateinit var datePicker: DatePickerDialog

    // Dates
    private var dateFromEpochDays: Long = 0L
    private var dateToEpochDays: Long = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogView = inflater.inflate(R.layout.dialog_date_select, container, false)
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        dateFromEpochDays = mainViewModel.getDateFromEpochDays().value!!
        dateToEpochDays = mainViewModel.getDateToEpochDays().value!!

        //Views
        btnApply = dialogView.findViewById(R.id.btn_apply)
        btnApply.setOnClickListener { applyDates() }

        btnCancel = dialogView.findViewById(R.id.btn_cancel)
        btnCancel.setOnClickListener { dismiss() }

        btnSelectDateFrom = dialogView.findViewById(R.id.iv_select_from)
        btnSelectDateFrom.setOnClickListener { showDateFromPicker() }

        btnSelectDateTo = dialogView.findViewById(R.id.iv_select_to)
        btnSelectDateTo.setOnClickListener { showDateToPicker() }

        tvDateFrom = dialogView.findViewById(R.id.tv_date_from_value)
        tvDateFrom.text = LocalDate.ofEpochDay(dateFromEpochDays).toString()

        tvDateTo = dialogView.findViewById(R.id.tv_date_to_value)
        tvDateTo.text = LocalDate.ofEpochDay(dateToEpochDays).toString()
    }

    private fun showDateFromPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker = DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateFrom(yy, mm, dd)},
            year,
            month,
            day
        )

        datePicker.datePicker.maxDate = dateToEpochDays * FACTOR_DAYS_TO_MILLISEC
        datePicker.show()
    }

    private fun setDateFrom(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth)

        tvDateFrom.text = dateString
        dateFromEpochDays = LocalDate.parse(dateString).toEpochDay()
    }

    private fun showDateToPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker = DatePickerDialog(requireActivity(),
            { _, yy, mm, dd -> setDateTo(yy, mm, dd)},
            year,
            month,
            day
        )

        datePicker.datePicker.minDate = dateFromEpochDays * FACTOR_DAYS_TO_MILLISEC
        datePicker.datePicker.maxDate = LocalDate.now().toEpochDay() * FACTOR_DAYS_TO_MILLISEC
        datePicker.show()
    }

    private fun setDateTo(year: Int, monthOfYear: Int, dayOfMonth: Int){
        val dateString = year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth)

        tvDateTo.text = dateString
        dateToEpochDays = LocalDate.parse(dateString).toEpochDay()
    }


    private fun applyDates(){
        mainViewModel.setDateRange(dateFromEpochDays, dateToEpochDays)
        dismiss()
    }
}