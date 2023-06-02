package com.carrati.mybills.extensions

fun Double?.toMoneyString(): String {
    if (this == null) return ""
    return "%.2f".format(this).replace(".", ",")
}

fun String?.toMoneyDouble(): Double {
    if (this.isNullOrBlank()) return 0.0
    return this.replace(",", ".").toDouble()
}
