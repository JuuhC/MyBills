package com.carrati.mybills.app.ui.transacoes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.transacoes.DeletarTransacaoUC
import com.carrati.domain.usecases.transacoes.ListarTransacoesUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import java.lang.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransacoesViewModel(
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val listarTransacoesUC: ListarTransacoesUC,
    private val deletarTransacaoUC: DeletarTransacaoUC
) : ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var transacoesLiveData = MutableLiveData<Response>()
    var deletarLiveData = MutableLiveData<Response>()

    fun getTransacoes(uid: String, periodo: String) {
        transacoesLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = listarTransacoesUC.execute(uid, periodo)
                transacoesLiveData.postValue(Response.success(list))
            } catch (e: Exception) {
                Log.e("exception transacoes", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                transacoesLiveData.postValue(Response.error(e))
            }
        }
    }

    fun deletarTransacao(uid: String, periodo: String, transacao: Transacao) {
        deletarLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                deletarTransacaoUC.execute(uid, periodo, transacao)
                deletarLiveData.postValue(Response.success(transacao))
            } catch (e: Exception) {
                Log.e("exception transacoes", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                deletarLiveData.postValue(Response.error(e))
            }
        }
    }

    fun getUsuario() {
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}
