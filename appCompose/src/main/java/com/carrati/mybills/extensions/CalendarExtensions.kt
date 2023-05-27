package com.carrati.mybills.appCompose.extensions

import java.util.*

var Calendar.year: Int
    get() = this.get(Calendar.YEAR)
    set(value) = this.set(Calendar.YEAR, value)

var Calendar.month: Int
    get() = this.get(Calendar.MONTH)
    set(value) = this.set(Calendar.MONTH, value)

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