package com.cyrilfind.kodo.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "content")
    var content: String = "",
    @Json(name = "due")
    var due: DueDate? = null,
    @Json(name = "due_date")
    var dueDate: String? = due?.date?.toString(), // hack for sending dates
    @Json(name = "completed")
    var completed: Boolean = false,
    @Json(name = "checked")
    var checked: Int? = null
) : Serializable {
    init {
        checked?.let { completed = checked == 1 } // hack for completed items
    }
}