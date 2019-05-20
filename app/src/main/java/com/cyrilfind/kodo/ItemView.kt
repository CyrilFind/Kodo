package com.cyrilfind.kodo

import android.content.Context
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.cyrilfind.kodo.R
import kotlinx.android.synthetic.main.item_view.view.*

class ItemView(context: Context) : LinearLayout(context) {
    val textView: TextView
    val deleteButton: ImageButton
    val checkbox: CheckBox

    init {
        inflate(context, R.layout.item_view, this)
        textView = itemTextView
        deleteButton = itemDeleteButton
        checkbox = itemCheckBox
    }
}