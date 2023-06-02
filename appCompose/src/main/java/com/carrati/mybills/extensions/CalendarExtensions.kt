package com.carrati.mybills.appCompose.extensions

import java.text.SimpleDateFormat
import java.util.*

var Calendar.year: Int
    get() = this.get(Calendar.YEAR)
    set(value) = this.set(Calendar.YEAR, value)

var Calendar.month: Int
    get() = run {
        this.get(Calendar.MONTH) + 1
    }
    set(value) = this.set(Calendar.MONTH, value - 1)

var Calendar.day: Int
    get() = this.get(Calendar.DAY_OF_MONTH)
    set(value) = this.set(Calendar.DAY_OF_MONTH, value)

fun Calendar.nextMonth(): Calendar {
    val clone = this.cloneCalendar()
    clone.add(Calendar.MONTH, 1)
    return clone
}

fun Calendar.previousMonth(): Calendar {
    val clone = this.cloneCalendar()
    clone.add(Calendar.MONTH, -1)
    return clone
}

fun Calendar.startDay(): Calendar {
    val calendar = this.cloneCalendar()
    calendar.day = 1
    return calendar
}

fun Calendar.endDay(): Calendar {
    val calendar = this.cloneCalendar()
    calendar.day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return calendar
}

fun Calendar.cloneCalendar(): Calendar {
    return this.clone() as Calendar
}

fun Calendar.toYearMonth(): String {
    return "${this.year}-${this.month}"
}

fun Calendar.toYearMonthDay(): String {
    return "${this.year}-${this.month}-${this.day}"
}

fun String?.toCalendar(format: String = "yyyy-MM-dd"): Calendar {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    val calendar = Calendar.getInstance()

    if (this.isNullOrBlank()) return calendar

    val date = dateFormat.parse(this)
    if (date != null) {
        calendar.time = date
    }

    return calendar
}
