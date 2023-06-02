package com.carrati.mybills.appCompose.ui.main.transactions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.usecases.transacoes.DeletarTransacaoUC
import com.carrati.domain.usecases.transacoes.ListarTransacoesUC
import com.carrati.mybills.appCompose.extensions.toYearMonth
import java.lang.Exception
import java.util.Calendar
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val listarTransacoesUC: ListarTransacoesUC,
    private val deletarTransacaoUC: DeletarTransacaoUC
) : ViewModel() {
    val state: MutableState<TransactionsViewState> = mutableStateOf(TransactionsViewState())

    fun loadData(userId: String, selectedDate: Calendar = state.value.selectedDate) {
        state.value = state.value.copy(isLoading = true, isError = false)
        viewModelScope.launch {
            try {
                val list = listarTransacoesUC.execute(userId, selectedDate.toYearMonth())
                state.value = state.value.copy(
                    isLoading = false,
                    isError = false,
                    transactionsAll = list,
                    transactionsFiltered = list,
                    selectedDate = selectedDate
                )
            } catch (e: Exception) {
                Log.e("exception listar transacoes", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                state.value = state.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    fun onDeleteTransaction(transacao: Transacao, userId: String) {
        state.value = state.value.copy(isLoading = true, isError = false)
        viewModelScope.launch {
            try {
                deletarTransacaoUC.execute(
                    userId,
                    state.value.selectedDate.toYearMonth(),
                    transacao
                )
                loadData(userId, state.value.selectedDate)
            } catch (e: Exception) {
                Log.e("exception deletar transacao", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                state.value = state.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    fun filterList(value: String) {
        val filterResult = state.value.transactionsAll.filter {
            it.nome?.contains(value, ignoreCase = true) ?: false
        }
        state.value = state.value.copy(transactionsFiltered = filterResult)
    }
}
