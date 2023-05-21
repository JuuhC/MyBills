package com.carrati.mybills.ui.main.home

import com.carrati.domain.models.Conta

data class HomeViewState(
    var isLoading: Boolean = false,
    var contas: List<Conta> = emptyList(),
    var saldo: Float = 0.0f,
    var receitas: Float = 0.0f,
    var despesas: Float = 0.0f
)