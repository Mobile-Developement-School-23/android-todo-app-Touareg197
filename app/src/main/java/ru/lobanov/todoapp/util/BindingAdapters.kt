package ru.lobanov.todoapp.util

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.lobanov.todoapp.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@SuppressLint("SetTextI18n")
@BindingAdapter("setPriority")
fun setPriority(view: ImageView, priority: String) {

    when (priority) {
        "important" -> {
            view.setImageResource(R.drawable.ic_high)
        }

        "low" -> {
            view.setImageResource(R.drawable.ic_low)
        }

        else -> {
            view.setImageResource(R.drawable.rectangle)
        }
    }
}

@BindingAdapter("setTimestamp")
fun setTimestamp(view: TextView, timestamp: Long) {
    view.text = if (timestamp != 0L) {
        SimpleDateFormat.getDateInstance().format(Date(timestamp * 1000L))
    } else {
        ""
    }
}

@BindingAdapter("setdeadline")
fun setDeadline(view: ImageView, timestamp: Long) {
    if (timestamp != 0.toLong()) {
        val firstDate: Long = timestamp
        val secondDate: Long = dateNow()
        val cmp = firstDate.compareTo(secondDate)

        when {
            cmp > 0 -> {
                view.setImageResource(R.drawable.ic_unchecked)
            }

            cmp < 0 -> {
                view.setImageResource(R.drawable.ic_uncheckedred)
            }

            else -> {}
        }
    }
}

@BindingAdapter("setChecked")
fun setChecked(view: ImageView, done: Boolean) {
    if (done) {
        view.setImageResource(R.drawable.ic_checked)
    }
}

@BindingAdapter("setCheckedText")
fun setCheckedText(view: TextView, done: Boolean) {
    view.paintFlags = if (done) {
        Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

fun convertDateToLong(date: String): Long {
    val l = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
    return l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
}

fun dateNow(): Long {
    return convertDateToLong(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
    )
}