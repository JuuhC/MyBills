package com.carrati.mybills.appCompose.ui.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.domain.models.Conta
import java.util.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val state: MutableState<HomeViewState> = mutableStateOf(HomeViewState())

    fun loadData(selectedDate: Calendar) {
        state.value.isLoading = true
        viewModelScope.launch {
            state.value.apply {
                this.isLoading = false
                this.contas = listOf(
                    Conta("Conta", 10.0),
                    Conta("Conta", 20.0),
                    Conta("Conta", 30.0)
                )
                this.saldo = 100f
                this.despesas = 100f
                this.receitas = 200f
                this.selectedDate = selectedDate
            }
        }
    }

    fun onAddConta() {
        loadData(state.value.selectedDate)
    }
}
