package com.carrati.mybills.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignOutFirebaseUC

class HomeViewModel(
    private val signOutFirebaseUC: SignOutFirebaseUC,
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC
) : ViewModel() {

    var usuarioLiveData: LiveData<Usuario>? = null

    fun signOutFirebase() {
        signOutFirebaseUC.execute()
    }

    fun getUsuario(){
        usuarioLiveData = obterUsuarioFirestoreUC.execute()
    }
}