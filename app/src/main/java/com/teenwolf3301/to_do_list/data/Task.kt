package com.teenwolf3301.to_do_list.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val isCompleted: Boolean = false,
    val priority: Priority,
    val date: Long = System.currentTimeMillis()
) : Parcelable {
    val formattedDate: String
        get() = DateFormat.getDateInstance().format(date)
}