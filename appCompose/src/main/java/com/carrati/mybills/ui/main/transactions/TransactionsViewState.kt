package com.carrati.mybills.appCompose.ui.main.transactions

import com.carrati.domain.models.Transacao
import java.util.*

data class TransactionsViewState(
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var transactionsByMonth: List<Transacao> = emptyList(),
    var selectedDate: Calendar = Calendar.getInstance()
)
