package com.carrati.mybills.ui.transacoes

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.transacoes.ListarTransacoesUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TransacoesViewModel(
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val listarTransacoesUC: ListarTransacoesUC
) : ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var transacoesLiveData = MutableLiveData<Response>()

    val loading = ObservableField<Boolean>(false)
    var isError = ObservableField<Boolean>(false)

    fun getTransacoes(uid: String, periodo: String, filter: String?) {
        transacoesLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = listarTransacoesUC.execute(uid, periodo, filter)
                transacoesLiveData.postValue(Response.success(list))
            } catch (e: Exception) {
                Log.e("exception transacoes", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                transacoesLiveData.postValue(Response.error(e))
            }
        }
    }

    fun getUsuario(){
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}