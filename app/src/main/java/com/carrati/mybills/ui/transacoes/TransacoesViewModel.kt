package com.carrati.mybills.ui.transacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.usecases.usuarios.SignOutFirebaseUC

class TransacoesViewModel(
    private val signOutFirebaseUC: SignOutFirebaseUC
) : ViewModel() {

    fun signOutFirebase() {
        signOutFirebaseUC.execute()
    }
}