package com.faramiki.humiditywhatch

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*


class DialogDateSelect(): DialogFragment() {

    private lateinit var dialogView:View
    private lateinit var mainViewModel: MainViewModel

    // Buttons:
    private lateinit var btnApply: Button
    private lateinit var btnCancel: Button

    // Date selectors
    private lateinit var etFrom: EditText
    private lateinit var etTo: EditText
    private lateinit var datePicker: DatePickerDialog



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
        initButtons()
        initEditTexts()
    }

    private fun initButtons()
    {
        btnApply = dialogView.findViewById(R.id.btn_apply)
        btnCancel = dialogView.findViewById(R.id.btn_cancel)

        btnApply.setOnClickListener { applyDates() }
        btnApply.isEnabled = false
        btnCancel.setOnClickListener { dismiss() }
    }

    private fun initEditTexts()
    {
        etFrom = dialogView.findViewById(R.id.et_date_from)
        etFrom.setOnClickListener { showDatePicker(etFrom) }
        etTo = dialogView.findViewById(R.id.et_date_to)
        etTo.setOnClickListener { showDatePicker(etTo) }

    }

    private fun showDatePicker(targetView:EditText) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker = DatePickerDialog(requireActivity(),
            { _, year, monthOfYear, dayOfMonth
                -> setDateFromPicker(targetView, year.toString() + "-" + "%02d".format(monthOfYear + 1) + "-" + "%02d".format(dayOfMonth))},
            year,
            month,
            day
        )
        datePicker.datePicker.maxDate = Date().time
        datePicker.show()
    }

    private fun setDateFromPicker(targetView:EditText, formattedDate: String){
        targetView.setText(formattedDate)
        btnApply.isEnabled = (!TextUtils.isEmpty(etFrom.text) && !TextUtils.isEmpty(etTo.text))
    }

    private fun applyDates()
    {
        val dateFromString = etFrom.text.toString()
        val dateToString = etTo.text.toString()

        val dateFromEpochSeconds = LocalDate.parse(dateFromString).toEpochDay() * 24 * 3600
        val dateToEpochSeconds = (LocalDate.parse(dateToString).toEpochDay() + 1) * 24 * 3600 - 1

        mainViewModel.setDateRange(dateFromEpochSeconds, dateToEpochSeconds)
        dismiss()

    }
}