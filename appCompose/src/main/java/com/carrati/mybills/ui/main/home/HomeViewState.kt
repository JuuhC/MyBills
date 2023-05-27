package com.carrati.mybills.appCompose.ui.main.home

import com.carrati.domain.models.Conta
import java.util.*

data class HomeViewState(
    var isLoading: Boolean = false,
    var contas: List<Conta> = emptyList(),
    var saldo: Float = 0.0f,
    var receitas: Float = 0.0f,
    var despesas: Float = 0.0f,
    var selectedDate: Calendar = Calendar.getInstance()
)