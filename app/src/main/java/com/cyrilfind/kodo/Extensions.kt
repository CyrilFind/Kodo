package com.cyrilfind.kodo

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

var TextView.strikeThrough
    get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0
    set(value) {
        paintFlags = if (value)
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

fun TextView.setTextOrGone(text: CharSequence?) {
    this.visibility = if (text.isNullOrBlank()) View.GONE else View.VISIBLE
    this.text = text
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}