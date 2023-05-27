package com.carrati.mybills.appCompose.ui.main.transactions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.domain.models.Transacao
import java.util.Calendar
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {
    val state: MutableState<TransactionsViewState> = mutableStateOf(TransactionsViewState())

    fun loadData(selectedDate: Calendar) {
        state.value.apply {
            isLoading = true
            isError = false
        }
        viewModelScope.launch {
            state.value.apply {
                this.isLoading = false
                this.transactionsByMonth = listOf(
                    Transacao(),
                    Transacao().apply { efetuado = true },
                    Transacao(),
                    Transacao().apply { efetuado = true },
                    Transacao()
                )
                this.selectedDate = selectedDate
            }
        }
    }
}
