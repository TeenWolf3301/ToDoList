package com.teenwolf3301.to_do_list.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "table_todo")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String = "Finance",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.LOW,
    val date: Long = System.currentTimeMillis()
) : Parcelable {
    val formattedDate: String
        get() = DateFormat.getDateInstance().format(date)
}