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
import com.carrati.mybills.appCompose.extensions.month
import com.carrati.mybills.appCompose.extensions.year
import java.lang.Exception
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val obterBalancoMensalUC: ObterBalancoMensalUC,
    private val listarContasUC: ListarContasUC,
    private val criarContaUC: CriarContaUC,
    private val userId: String
) : ViewModel() {
    val state: MutableState<HomeViewState> = mutableStateOf(HomeViewState())

    fun loadData(selectedDate: Calendar) {
        state.value = state.value.copy(isLoading = true)
        viewModelScope.launch {
            val dispatcher = Dispatchers.IO
            val list = withContext(dispatcher) { listarContasUC.execute(userId) }
            val balancoMensal = withContext(dispatcher) {
                val selectedPeriod = "${selectedDate.year}-${selectedDate.month}"
                obterBalancoMensalUC.execute(userId, selectedPeriod)
            }
            state.value = state.value.copy(
                isLoading = false,
                contas = list,
                saldo = list.sumOf { it.saldo ?: 0.0 },
                receitas = balancoMensal["receitas"] ?: 0.0,
                despesas = balancoMensal["despesas"] ?: 0.0,
                selectedDate = selectedDate
            )
        }
    }

    fun onAddConta(accountName: String, initialValue: Double) {
        state.value = state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                criarContaUC.execute(userId, Conta(accountName, initialValue))
                state.value = state.value.copy(isLoading = false, errorMessage = "")
            } catch (e: Exception) {
                Log.e("exception cria conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                state.value = state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao criar conta: $e"
                )
            }
        }
        loadData(state.value.selectedDate)
    }
}
