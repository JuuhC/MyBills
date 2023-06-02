package com.carrati.mybills.appCompose.ui.main.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Conta
import com.carrati.domain.usecases.contas.CriarContaUC
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.ObterBalancoMensalUC
import com.carrati.mybills.appCompose.extensions.toYearMonth
import java.lang.Exception
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val obterBalancoMensalUC: ObterBalancoMensalUC,
    private val listarContasUC: ListarContasUC,
    private val criarContaUC: CriarContaUC
) : ViewModel() {
    val state: MutableState<HomeViewState> = mutableStateOf(HomeViewState())

    fun loadData(userId: String, selectedDate: Calendar = state.value.selectedDate) {
        state.value = state.value.copy(isLoading = true, isError = false)
        viewModelScope.launch {
            try {
                val dispatcher = Dispatchers.IO
                val list = withContext(dispatcher) {
                    listarContasUC.execute(userId)
                }
                val balancoMensal = withContext(dispatcher) {
                    obterBalancoMensalUC.execute(userId, selectedDate.toYearMonth())
                }
                state.value = state.value.copy(
                    isLoading = false,
                    isError = false,
                    contas = list,
                    saldo = list.sumOf { it.saldo ?: 0.0 },
                    receitas = balancoMensal["receitas"] ?: 0.0,
                    despesas = balancoMensal["despesas"] ?: 0.0,
                    selectedDate = selectedDate
                )
            } catch (e: Exception) {
                Log.e("exception carregar tela", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                state.value = state.value.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    fun onAddConta(
        userId: String,
        accountName: String,
        initialValue: Double,
        onError: () -> Unit
    ) {
        state.value = state.value.copy(isLoading = true, isError = false)
        viewModelScope.launch {
            try {
                criarContaUC.execute(userId, Conta(accountName, initialValue))
                loadData(userId, state.value.selectedDate)
            } catch (e: Exception) {
                Log.e("exception cria conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                onError()
            }
        }
    }
}
