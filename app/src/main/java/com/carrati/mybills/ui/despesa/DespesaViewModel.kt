package com.carrati.mybills.ui.despesa

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.transacoes.CadastrarReceitaDespesaUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DespesaViewModel(
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val cadastrarReceitaDespesaUC: CadastrarReceitaDespesaUC
): ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var despesaLiveData = MutableLiveData<Response>()

    val loading = ObservableField<Boolean>(false)

    fun salvarTransacao(uid: String, periodo: String, transacao: Transacao) {
        despesaLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cadastrarReceitaDespesaUC.execute(uid, periodo, transacao)
                despesaLiveData.postValue(Response.success(true))
            } catch (e: Exception) {
                Log.e("exception save despesa", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                despesaLiveData.postValue(Response.error(e))
            }
        }
    }

    fun getUsuario(){
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}