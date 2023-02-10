package com.carrati.mybills.ui.receita

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.CadastrarReceitaDespesaUC
import com.carrati.domain.usecases.transacoes.EditarTransacaoUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import java.lang.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceitaViewModel(
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val cadastrarReceitaDespesaUC: CadastrarReceitaDespesaUC,
    private val listarContasUC: ListarContasUC,
    private val editarTransacaoUC: EditarTransacaoUC
) : ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var receitaLiveData = MutableLiveData<Response>()
    var listarContasLiveData = MutableLiveData<Response>()

    val loading = ObservableField(false)

    fun salvarTransacao(uid: String, periodo: String, transacao: Transacao) {
        receitaLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cadastrarReceitaDespesaUC.execute(uid, periodo, transacao)
                receitaLiveData.postValue(Response.success(true))
            } catch (e: Exception) {
                Log.e("exception save receita", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                receitaLiveData.postValue(Response.error(e))
            }
        }
    }

    fun carregarContas(uid: String) {
        listarContasLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = listarContasUC.execute(uid)
                listarContasLiveData.postValue(Response.success(list))
            } catch (e: Exception) {
                Log.e("exception listar conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                listarContasLiveData.postValue(Response.error(e))
            }
        }
    }

    fun editarTransacao(
        uid: String,
        periodo: String,
        transacao: Transacao,
        transacaoAntiga: Transacao? = null
    ) {
        receitaLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                editarTransacaoUC.execute(uid, periodo, transacao, transacaoAntiga)
                receitaLiveData.postValue(Response.success(true))
            } catch (e: Exception) {
                Log.e("exception save despesa", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                receitaLiveData.postValue(Response.error(e))
            }
        }
    }

    fun getUsuario() {
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}
