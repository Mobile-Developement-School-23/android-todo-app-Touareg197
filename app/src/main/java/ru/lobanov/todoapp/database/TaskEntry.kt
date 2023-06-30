package ru.lobanov.todoapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "tasks_table")
data class TaskEntry(
    @PrimaryKey(autoGenerate = true)
    val ids: Int,
    var changed_at: Long,
    val color: String,
    val created_at: Long,
    val deadline: Long,
    var done: Boolean,
    val id: String,
    val importance: String,
    val last_updated_by: String,
    val text: String
) : Parcelable