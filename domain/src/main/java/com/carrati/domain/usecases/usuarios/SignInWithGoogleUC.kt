package com.carrati.domain.usecases.usuarios

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository
import com.google.firebase.auth.AuthCredential

class SignInWithGoogleUC(private val repo: IUsuariosRepository) {

    fun execute(googleAuthCredential: AuthCredential?): MutableLiveData<Usuario>?{
        return repo.firebaseSignInWithGoogle(googleAuthCredential)
    }
}