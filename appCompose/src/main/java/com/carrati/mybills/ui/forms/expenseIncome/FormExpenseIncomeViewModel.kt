package com.carrati.mybills.appCompose.ui.newTransaction.expense

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.CadastrarReceitaDespesaUC
import com.carrati.domain.usecases.transacoes.EditarTransacaoUC
import com.carrati.mybills.appCompose.extensions.toCalendar
import com.carrati.mybills.appCompose.extensions.toYearMonth
import com.carrati.mybills.appCompose.extensions.toYearMonthDay
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionViewState
import com.carrati.mybills.extensions.toMoneyDouble
import com.carrati.mybills.extensions.toMoneyString
import java.lang.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormExpenseIncomeViewModel(
    private val transactionType: TransactionTypeEnum,
    private val oldTransaction: Transacao?,
    private val userId: String,
    private val cadastrarReceitaDespesaUC: CadastrarReceitaDespesaUC,
    private val listarContasUC: ListarContasUC,
    private val editarTransacaoUC: EditarTransacaoUC
) : ViewModel() {

    val state: MutableState<FormTransactionViewState> = mutableStateOf(FormTransactionViewState())

    init {
        if (oldTransaction != null) {
            state.value = state.value.copy(
                amount = oldTransaction.valor.toMoneyString(),
                paid = oldTransaction.efetuado ?: false,
                date = oldTransaction.data.toCalendar(),
                description = oldTransaction.nome ?: "",
                account1 = oldTransaction.conta ?: ""
            )
        }

        carregarContas()
    }

    fun carregarContas() {
        state.value = state.value.copy(loading = true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = listarContasUC.execute(userId).mapNotNull { it.nome }
                state.value = state.value.copy(loading = false, listAccounts = list)
            } catch (e: Exception) {
                Log.e("exception listar conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
            }
        }
    }

    fun salvarTransacao(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (oldTransaction != null) {
            editarTransacao(onSuccess, onError)
        } else {
            criarTransacao(onSuccess, onError)
        }
    }

    private fun criarTransacao(onSuccess: () -> Unit, onError: (String) -> Unit) {
        state.value = state.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val transacao = Transacao().apply {
                    this.tipo = transactionType.nome
                    this.data = state.value.date.toYearMonthDay()
                    this.nome = state.value.description
                    this.valor = state.value.amount.toMoneyDouble()
                    this.conta = state.value.account1
                    this.efetuado = state.value.paid
                }
                cadastrarReceitaDespesaUC.execute(userId, state.value.date.toYearMonth(), transacao)
                onSuccess()
            } catch (e: Exception) {
                Log.e("exception save despesa", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                onError("Erro ao salvar despesa: $e")
            }
        }
    }

    private fun editarTransacao(onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.e("editarTransacao", "chamando...")
        state.value = state.value.copy(loading = true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newTransaction = Transacao().apply {
                    this.id = oldTransaction?.id
                    this.tipo = oldTransaction?.tipo
                    this.data = state.value.date.toYearMonthDay()
                    this.nome = state.value.description
                    this.valor = state.value.amount.toMoneyDouble()
                    this.conta = state.value.account1
                    this.efetuado = state.value.paid
                }
                editarTransacaoUC.execute(
                    userId,
                    state.value.date.toYearMonth(),
                    newTransaction,
                    oldTransaction
                )
                onSuccess()
            } catch (e: Exception) {
                Log.e("exception edit despesa", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                onError("Erro ao salvar despesa: $e")
            }
        }
    }
}
