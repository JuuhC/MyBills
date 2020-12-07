package com.carrati.domain.usecases.usuarios

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository

class SignOutFirebaseUC(private val repo: IUsuariosRepository) {

    fun execute(){
        return repo.signOutFirebase()
    }
}