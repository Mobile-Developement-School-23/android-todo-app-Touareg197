package ru.lobanov.todoapp.ui.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.databinding.FragmentCreateToDoBinding
import ru.lobanov.todoapp.viewmodel.AddViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class CreateToDoFragment : Fragment() {

    private val viewModel: AddViewModel by viewModels()
    lateinit var binding: FragmentCreateToDoBinding
    var calendar: Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_date)

        binding = FragmentCreateToDoBinding.inflate(inflater)

        setSpinner()
        saveClickListener()
        closeClickListener()
        setDate()
        return binding.root
    }

    private fun setDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(binding.date)
            }

        binding.dateSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (binding.dateSwitch.isChecked) {
                val dpd = DatePickerDialog(
                    requireActivity(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dpd.datePicker.minDate = calendar.time.time
                dpd.show()
            } else
                binding.date.text = ""
        }
    }

    private fun closeClickListener() {
        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createToDoFragment_to_toDoListFragment)
        }
    }

    private fun saveClickListener() {
        binding.save.setOnClickListener {
            if (TextUtils.isEmpty((binding.dealtext.text))) {
                Toast.makeText(requireContext(), "Здесь пусто", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.getList()

            val titleStr = binding.dealtext.text.toString()
            val priority = spinnerSelectedItemId()
            var dates: Long = 0
            if (binding.date.text.toString() != "")
                dates = convertDateToLong(binding.date.text.toString())

            var id: Int = 0
            viewModel.getAllTasks.observe(viewLifecycleOwner) { it ->
                it.forEach {
                    if (it.ids > id)
                        id = it.ids
                }
                id = if (it.isNotEmpty()) {
                    id + 1
                } else {
                    0
                }

                viewModel.insert(
                    titleStr,
                    priority,
                    dates,
                    dateNow(),
                    "",
                    dateNow(),
                    false,
                    id.toString(),
                    binding.date.text.toString()
                )
            }
            Toast.makeText(requireContext(), "Успешно создано", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_createToDoFragment_to_toDoListFragment)
        }
    }

    private fun setSpinner() {
        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.importance_list)
        )
        binding.spinner.adapter = myAdapter
    }

    private fun dateNow(): Long {
        return convertDateToLong(
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        )
    }

    private fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        return l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
    }

    private fun spinnerSelectedItemId(): String {
        var important = ""
        val spinnerID = binding.spinner.selectedItemId.toString()
        if (spinnerID == "0")
            important = getString(R.string.basic)
        if (spinnerID == "1")
            important = getString(R.string.low)
        if (spinnerID == "2")
            important = getString(R.string.important)
        return important
    }

    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy hh:mm:ss"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        val periodDate = calendar.time

        date.text = periodDate?.let { sdf.format(it) }
    }

}