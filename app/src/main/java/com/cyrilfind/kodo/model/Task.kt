package com.cyrilfind.kodo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Task(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    var title: String? = "",
    @SerialName("description")
    var description: String? = ""
) : Parcelable