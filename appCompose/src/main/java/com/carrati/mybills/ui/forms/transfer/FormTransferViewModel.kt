package com.carrati.mybills.appCompose.ui.newTransaction.income

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.CadastrarTransferenciaUC
import com.carrati.mybills.appCompose.extensions.toYearMonth
import com.carrati.mybills.appCompose.extensions.toYearMonthDay
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionViewState
import com.carrati.mybills.extensions.toMoneyDouble
import java.lang.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormTransferViewModel(
    private val userId: String,
    private val cadastrarTransferenciaUC: CadastrarTransferenciaUC,
    private val listarContasUC: ListarContasUC
) : ViewModel() {

    val state: MutableState<FormTransactionViewState> = mutableStateOf(FormTransactionViewState())

    init {
        carregarContas()
    }

    private fun carregarContas() {
        state.value = state.value.copy(loading = true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = listarContasUC.execute(userId).mapNotNull { it.nome }
                state.value = state.value.copy(loading = false, listAccounts = list)
            } catch (e: Exception) {
                Log.e("exception listar conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                state.value = state.value.copy(loading = true)
            }
        }
    }

    fun salvarTransferencia(onSuccess: () -> Unit, onError: (String) -> Unit) {
        state.value = state.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val transacaoSaida = Transacao().apply {
                    this.tipo = TransactionTypeEnum.EXPENSE.nome
                    this.data = state.value.date.toYearMonthDay()
                    this.nome = "Transferência para ${state.value.account2}"
                    this.valor = state.value.amount.toMoneyDouble()
                    this.conta = state.value.account1
                    this.efetuado = true
                }
                val transacaoEntrada = Transacao().apply {
                    this.tipo = TransactionTypeEnum.INCOME.nome
                    this.data = state.value.date.toYearMonthDay()
                    this.nome = "Transferência de ${state.value.account1}"
                    this.valor = state.value.amount.toMoneyDouble()
                    this.conta = state.value.account2
                    this.efetuado = true
                }
                cadastrarTransferenciaUC.execute(
                    userId,
                    state.value.date.toYearMonth(),
                    transacaoSaida,
                    transacaoEntrada
                )
                onSuccess()
            } catch (e: Exception) {
                Log.e("exception save transferencia", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                onError("Erro ao salvar transferência: $e")
            }
        }
    }
}
