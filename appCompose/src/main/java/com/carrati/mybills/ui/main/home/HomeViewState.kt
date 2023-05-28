package com.carrati.mybills.appCompose.ui.main.home

import com.carrati.domain.models.Conta
import java.util.*

data class HomeViewState(
    var isLoading: Boolean = false,
    var errorMessage: String = "",
    var contas: List<Conta> = emptyList(),
    var saldo: Double = 0.0,
    var receitas: Double = 0.0,
    var despesas: Double = 0.0,
    var selectedDate: Calendar = Calendar.getInstance()
)
