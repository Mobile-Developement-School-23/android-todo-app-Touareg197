package ru.lobanov.todoapp.ui.modify

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
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.databinding.FragmentModifyToDoBinding
import ru.lobanov.todoapp.viewmodel.UpdateViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ModifyToDoFragment : Fragment() {

    private val viewModel: UpdateViewModel by viewModels()
    lateinit var binding: FragmentModifyToDoBinding
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTheme(R.style.Theme_date)
        binding = FragmentModifyToDoBinding.inflate(inflater)

        setSpinner()
        deleteText()
        saveOnClick()
        setPreset()
        closeClick()
        setDate()
        changeGarbageColor()
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

    private fun closeClick() {
        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_modifyToDoFragment_to_toDoListFragment)
        }
    }

    private fun setPreset() {
        val args = ModifyToDoFragmentArgs.fromBundle(requireArguments())
        binding.dealtext.setText(args.taskEntry.text)

        if (args.taskEntry.importance == getString(R.string.important))
            binding.spinner.setSelection(2)
        if (args.taskEntry.importance == getString(R.string.basic))
            binding.spinner.setSelection(0)
        if (args.taskEntry.importance == getString(R.string.low))
            binding.spinner.setSelection(1)

        if (args.taskEntry.deadline != 0.toLong())
            binding.date.text = convertLongToTime(args.taskEntry.deadline)
    }

    private fun saveOnClick() {
        val args = ModifyToDoFragmentArgs.fromBundle(requireArguments())
        binding.save.setOnClickListener {
            if (TextUtils.isEmpty(binding.dealtext.text)) {
                Toast.makeText(requireContext(), "Здесь пусто", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskStr = binding.dealtext.text.toString()

            var important = ""
            val spinnerID = binding.spinner.selectedItemId.toString()
            if (spinnerID == "0")
                important = getString(R.string.basic)
            if (spinnerID == "1")
                important = getString(R.string.low)
            if (spinnerID == "2")
                important = getString(R.string.important)
            var deadline: Long = 0
            if (binding.date.text != "")
                deadline = convertDateToLong(binding.date.text as String)

            viewModel.update(
                args.taskEntry.ids,
                taskStr,
                important,
                deadline,
                args.taskEntry.created_at,
                "",
                dateNow(),
                false,
                args.taskEntry.id,
                binding.date.text.toString()
            )
            Toast.makeText(requireContext(), "Успешно обновлено", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_modifyToDoFragment_to_toDoListFragment)
        }
    }

    private fun deleteText() {
        val args = ModifyToDoFragmentArgs.fromBundle(requireArguments())
        binding.deleteText.setOnClickListener {
            viewModel.delete(args.taskEntry)
            findNavController().navigate(R.id.action_modifyToDoFragment_to_toDoListFragment)
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

    private fun convertLongToTime(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val date = Date(time * 1000)
        return sdf.format(date)
    }

    private fun convertDateToLong(date: String): Long {
        val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        return l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
    }

    private fun dateNow(): Long {
        return convertDateToLong(
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        )
    }

    private fun updateDateInView(date: TextView) {
        val myFormat = "dd/MM/yyyy hh:mm:ss"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        val periodDate = calendar.time

        date.text = periodDate?.let { sdf.format(it) }
    }

    private fun changeGarbageColor() {
        if (binding.dealtext.text.toString().isNotEmpty()) {
            binding.deleteText.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.Red
                )
            )
            binding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        binding.dealtext.addTextChangedListener {
            if (binding.dealtext.text.toString().isNotEmpty() && !arguments?.getBoolean("new")!!) {
                binding.deleteText.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Red
                    )
                )
                binding.deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                binding.deleteText.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Label_Disable
                    )
                )
                binding.deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }
        }

        if (binding.dealtext.lineCount == 4) {
            binding.textcard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}