package ru.lobanov.todoapp.retrofit.model

import com.google.gson.annotations.SerializedName


data class Todo(
    @SerializedName("changed_at") var changed_at: Long = 0,
    @SerializedName("color") val color: String = "",
    @SerializedName("created_at") var created_at: Long,
    @SerializedName("deadline") var deadline: Long = 0,
    @SerializedName("done") var done: Boolean = false,
    @SerializedName("id") var id: String,
    @SerializedName("importance") var importance: String,
    @SerializedName("last_updated_by") val last_updated_by: String,
    @SerializedName("text") var text: String
)