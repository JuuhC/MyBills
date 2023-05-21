package com.carrati.mybills.app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Conta
import com.carrati.domain.models.Response
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.contas.CriarContaUC
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.ObterBalancoMensalUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignOutFirebaseUC
import java.lang.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val signOutFirebaseUC: SignOutFirebaseUC,
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC,
    private val obterBalancoMensalUC: ObterBalancoMensalUC,
    private val listarContasUC: ListarContasUC,
    private val criarContaUC: CriarContaUC
) : ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null
    var balancoLiveData = MutableLiveData<Response>()
    var listarContasLiveData = MutableLiveData<Response>()
    val criarContaLiveData = MutableLiveData<Response>()

    fun signOutFirebase() {
        signOutFirebaseUC.execute()
    }

    fun getUsuario() {
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
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

    fun carregarBalanco(uid: String, periodo: String) {
        balancoLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = obterBalancoMensalUC.execute(uid, periodo)
                balancoLiveData.postValue(Response.success(list))
            } catch (e: Exception) {
                Log.e("exception balanco", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                balancoLiveData.postValue(Response.error(e))
            }
        }
    }

    fun criarConta(uid: String, conta: Conta) {
        criarContaLiveData.postValue(Response.loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                criarContaUC.execute(uid, conta)
                criarContaLiveData.postValue(Response.success(true))
            } catch (e: Exception) {
                Log.e("exception cria conta", e.toString())
                FirebaseAPI().sendThrowableToFirebase(e)
                criarContaLiveData.postValue(Response.error(e))
            }
        }
    }
}
