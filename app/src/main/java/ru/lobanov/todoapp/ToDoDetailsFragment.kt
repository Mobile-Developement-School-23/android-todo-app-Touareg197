package ru.lobanov.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.lobanov.todoapp.R
import ru.lobanov.todoapp.model.TodoItemsRepository
import ru.lobanov.todoapp.model.TodoItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ToDoDetailsFragment : Fragment() {

    var new = true
    var change = false
    var calendar: Calendar = Calendar.getInstance()
    var normalDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_to_do_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTheme(R.style.Theme_date)

        val card: CardView = view.findViewById(R.id.textcard)
        val title: EditText = view.findViewById(R.id.dealtext)
        val deleteText: TextView = view.findViewById(R.id.deletetext)
        val deleteImg: ImageView = view.findViewById(R.id.deleteImg)
        val closeBtn: ImageButton = view.findViewById(R.id.closeBtn)

        if (title.text.toString() != "") {
            deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
            deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }

        if (title.lineCount == 4) {
            card.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        /** Инициализация спиннера **/
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.importance_list,

            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val switch: SwitchCompat = view.findViewById(R.id.dateSwitch)

        val date: TextView = view.findViewById(R.id.date)
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(date)
            }

        /** Свитч предназначен для изменения параметра даты  **/
        switch.setOnCheckedChangeListener { compoundButton, b ->
            if (switch.isChecked) {
                if (new) {
                    val dpd = DatePickerDialog(
                        requireActivity(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    dpd.datePicker.minDate = calendar.time.time
                    dpd.show()
                }
            } else
                date.text = ""
        }

        /** Получение данных с предыдущего фрагмента   **/
        val arr = arguments?.getParcelable<TodoItemsRepository>("MyArgChange")
        val id = arguments?.getInt("id")
        val newItem = arguments?.getBoolean("new")

        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        var text: TodoItem = TodoItem(
            id = "0",
            "",
            "",
            done = false,
            created_at = dateTime
        )
        if (newItem == false) {
            text = arr!![id!!]
        }

        /** Заолнение ui если мы редактируем дело **/
        if (newItem == false) {
            normalDate = text.deadline
            change = true
            new = false
            title.setText(text.title)
            if (text.deadline != "") {
                date.text = (text.deadline)
                switch.isChecked = true
                new = true
            }
            if (text.importance == "important")
                spinner.setSelection(2)
            if (text.importance == "basic")
                spinner.setSelection(0)
            if (text.importance == "low")
                spinner.setSelection(1)

        }
        /** Кнопка сохранение нового или измененного дела  **/
        val saveBtn: TextView = view.findViewById(R.id.save)
        saveBtn.setOnClickListener {
            if (title.text.toString() != "") {
                val bundle = Bundle()
                text.title = title.text.toString()
                text.changed_at = LocalDateTime.now()

                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                if (switch.isChecked)
                    text.deadline = normalDate
                else
                    text.deadline = ""
                if (spinner.selectedItemPosition == 0)
                    text.importance = "basic"
                if (spinner.selectedItemPosition == 1)
                    text.importance = "low"
                if (spinner.selectedItemPosition == 2)
                    text.importance = "important"

                if (newItem == false) {
                    if (id != null) {
                        arr?.set(id, text)
                        bundle.putParcelable("Change", arr)
                        view.findNavController().navigate(R.id.ToDoListFragment, bundle)
                    }
                } else {
                    if (arr != null) {
                        text.id = (arr.size + 1).toString()
                        arr.add(0, text)
                        bundle.putParcelable("Change", arr)
                    }
                    view.findNavController().navigate(R.id.ToDoListFragment, bundle)
                }
            }
        }
        /** Кнопка закрытия  **/
        closeBtn.setOnClickListener {
            //replaceFragment(ToDoListFragment())
            val bundle = Bundle()
            bundle.putParcelable("Change", arr)
            findNavController().navigate(R.id.ToDoListFragment, bundle)
        }
        /** Кнопка уцдаления не работает если запускаем наш фрагмента
         * с FABа добавления **/
        deleteText.setOnClickListener {
            if (title.text.toString() != "") {
                if (newItem == false) {
                    val bundle = Bundle()
                    arr?.removeAt(id!!)
                    bundle.putParcelable("Change", arr)
                    findNavController().navigate(R.id.ToDoListFragment, bundle)
                }
            }
        }
        deleteImg.setOnClickListener {
            if (title.text.toString() != "") {
                if (newItem == false) {
                    val bundle = Bundle()
                    arr?.removeAt(id!!)
                    bundle.putParcelable("Change", arr)
                    findNavController().navigate(R.id.ToDoListFragment, bundle)
                }
            }
        }
        if (newItem == false) {
            deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
            deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
        }
        /** Листенер для изменение TextView и ImageView "удалить" **/
        title.addTextChangedListener {
            if (title.text.toString() != "" && newItem == false) {
                deleteText.setTextColor(getColor(requireActivity(), R.color.Red))
                deleteImg.setImageResource(R.drawable.ic_baseline_deletered_24)
            } else {
                deleteText.setTextColor(getColor(requireActivity(), R.color.Label_Disable))
                deleteImg.setImageResource(R.drawable.ic_baseline_delete_24)
            }

            if (title.lineCount == 4) {
                card.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
    }

    private fun updateDateInView(date: TextView) {
        val myFormat = "dd"
        val myFormat2 = "yyyy"
        val normalFormat = "dd/MM/yyyy"
        val normalSimpleDateFormat = SimpleDateFormat(normalFormat, Locale.ENGLISH)
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        val sdf2 = SimpleDateFormat(myFormat2, Locale.ENGLISH)
        val periodDate = calendar.time
        normalDate = normalSimpleDateFormat.format(periodDate).toString()
        val month: String = calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG_FORMAT, Locale("ru")
        ) as String
        date.text = periodDate.let { sdf.format(it) + " " + month + " " + sdf2.format(it) }
    }
}