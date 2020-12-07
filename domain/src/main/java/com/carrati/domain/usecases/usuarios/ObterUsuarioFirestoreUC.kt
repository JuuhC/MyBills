package com.carrati.domain.usecases.usuarios

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository
import com.google.firebase.auth.AuthCredential

class ObterUsuarioFirestoreUC(private val repo: IUsuariosRepository) {

    suspend fun execute(uid: String): MutableLiveData<Usuario?>{
        return repo.getUserFromFirestore(uid)
    }
}