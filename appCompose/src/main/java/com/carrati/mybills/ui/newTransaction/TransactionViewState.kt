package com.carrati.mybills.appCompose.ui.newTransaction

data class TransactionViewState(
    var amount: Float = 0.0f,
    var paid: Boolean = false,
    var date: String = "",
    var description: String = "",
    var account1: String = "",
    var account2: String = "",
    val listAccounts: List<String> = emptyList()
)
