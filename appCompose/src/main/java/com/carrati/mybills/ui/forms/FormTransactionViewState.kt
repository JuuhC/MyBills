package com.carrati.mybills.appCompose.ui.newTransaction

import java.util.*

data class FormTransactionViewState(
    var loading: Boolean = false,
    var amount: Double = 0.0,
    var paid: Boolean = false,
    var date: Calendar = Calendar.getInstance(),
    var description: String = "",
    var account1: String = "",
    var account2: String = "",
    val listAccounts: List<String> = emptyList()
)
