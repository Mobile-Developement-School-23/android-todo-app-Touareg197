package ru.lobanov.todoapp.model

import android.os.Parcel
import android.os.Parcelable

data class TodoItem(
    var id: String,
    var title: String,
    var importance: String,
    var deadline: String = "01/01/2100",
    val done: Boolean,
    val created_at: String,
    var changed_at: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(importance)
        parcel.writeString(deadline)
        parcel.writeByte(if (done) 1 else 0)
        parcel.writeString(created_at)
        parcel.writeString(changed_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItem> {
        override fun createFromParcel(parcel: Parcel): TodoItem {
            return TodoItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as TodoItem
        if (id.toInt() != other.id.toInt()) {
            return false
        }
        if (title != other.title) {
            return false
        }
        if (importance != importance) {
            return false
        }
        if (deadline != other.deadline) {
            return false
        }
        if (done != other.done) {
            return false
        }
        if (created_at != other.created_at) {
            return false
        }
        if (changed_at != other.changed_at) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + importance.hashCode()
        result = 31 * result + deadline.hashCode()
        result = 31 * result + done.hashCode()
        result = 31 * result + created_at.hashCode()
        result = 31 * result + changed_at.hashCode()
        return result
    }
}