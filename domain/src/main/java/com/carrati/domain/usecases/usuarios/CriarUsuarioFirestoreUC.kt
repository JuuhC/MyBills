package com.carrati.domain.usecases.usuarios

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository
import com.google.firebase.auth.AuthCredential

class CriarUsuarioFirestoreUC(private val repo: IUsuariosRepository) {

    fun execute(user: Usuario): MutableLiveData<Usuario>?{
        return repo.createUserInFirestoreIfNotExists(user)
    }
}