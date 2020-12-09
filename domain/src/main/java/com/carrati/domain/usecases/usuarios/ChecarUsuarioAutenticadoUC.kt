package com.carrati.domain.usecases.usuarios

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository

class ChecarUsuarioAutenticadoUC(private val repo: IUsuariosRepository) {

    fun execute(): MutableLiveData<Usuario>?{
        return repo.checkIfUserIsAuthenticatedInFirebase()
    }
}