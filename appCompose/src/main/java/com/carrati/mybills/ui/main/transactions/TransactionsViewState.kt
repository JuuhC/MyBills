package com.carrati.mybills.appCompose.ui.main.transactions

import com.carrati.domain.models.Transacao
import java.util.*

data class TransactionsViewState(
    var isLoading: Boolean = true,
    var isError: Boolean = false,
    var transactionsAll: List<Transacao> = emptyList(),
    var transactionsFiltered: List<Transacao> = emptyList(),
    var selectedDate: Calendar = Calendar.getInstance()
)
