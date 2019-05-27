package com.cyrilfind.kodo.model

import android.annotation.SuppressLint
import android.text.format.DateFormat
import com.squareup.moshi.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "content")
    var content: String = "",
    @Json(name = "due")
    val due: DueDate? = null, // To GET due dates
    @TaskDueDate
    @Json(name = "due_date")
    var dueDate: Date? = null, // to POST due dates
    @Json(name = "completed")
    var completed: Boolean = false,
    @Json(name = "checked")
    var checked: Int? = null
) : Serializable {

    init {
        dueDate = due?.date
        checked?.let { completed = checked == 1 } // hack for completed items
    }
}


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class TaskDueDate

class TaskDueDateAdapter {
    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }

    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    @SuppressLint("SimpleDateFormat")
    @ToJson
    fun toJson(@TaskDueDate date: Date?): String? {
        return DateFormat.format(DATE_PATTERN, date).toString()
    }

    @FromJson
    @TaskDueDate
    fun fromJson(dueDate: String?): Date? {
        return simpleDateFormat.parse(dueDate)
    }
}