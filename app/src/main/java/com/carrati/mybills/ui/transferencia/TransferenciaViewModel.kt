package com.carrati.mybills.ui.transferencia

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.transacoes.CadastrarTransferenciaUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TransferenciaViewModel(
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val cadastrarTransferenciaUC: CadastrarTransferenciaUC
): ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var transferenciaLiveData = MutableLiveData<Response>()

    val loading = ObservableField<Boolean>(false)

    fun salvarTransacao(uid: String, periodo: String, transacao1: Transacao, transacao2: Transacao) {
        transferenciaLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cadastrarTransferenciaUC.execute(uid, periodo, transacao1, transacao2)
                transferenciaLiveData.postValue(Response.success(true))
            } catch (e: Exception) {
                Log.e("exception save transferencia", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                transferenciaLiveData.postValue(Response.error(e))
            }
        }
    }

    fun getUsuario(){
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}