package com.cyrilfind.kodo

import android.widget.CalendarView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import java.util.*

@BindingAdapter(value = ["android:onSelectedDayChange", "android:dateAttrChanged"], requireAll = false)
fun setListeners(
    view: CalendarView, onDayChange: CalendarView.OnDateChangeListener?,
    attrChange: InverseBindingListener?
) {
    if (attrChange == null) {
        view.setOnDateChangeListener(onDayChange)
    } else {
        view.setOnDateChangeListener { view, year, month, dayOfMonth ->
            onDayChange?.onSelectedDayChange(view, year, month, dayOfMonth)
            val instance = Calendar.getInstance()
            instance.set(year, month, dayOfMonth)
            view.date = instance.timeInMillis
            attrChange.onChange()
        }
    }
}