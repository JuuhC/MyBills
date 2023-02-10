package com.carrati.mybills.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.usuarios.ChecarUsuarioAutenticadoUC
import com.carrati.domain.usecases.usuarios.CriarUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignInWithGoogleUC
import com.google.firebase.auth.AuthCredential

class LoginViewModel(
    private val checarUsuarioAutenticadoUC: ChecarUsuarioAutenticadoUC,
    private val criarUsuarioFirestoreUC: CriarUsuarioFirestoreUC,
    private val signInWithGoogleUC: SignInWithGoogleUC
) : ViewModel() {

    var isUserAuthenticatedLiveData: LiveData<Usuario>? = null
    var authenticatedUserLiveData: LiveData<Usuario>? = null
    var createdUserLiveData: LiveData<Usuario>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = checarUsuarioAutenticadoUC.execute()
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = signInWithGoogleUC.execute(googleAuthCredential)
    }

    fun createUserFirestore(user: Usuario) {
        createdUserLiveData = criarUsuarioFirestoreUC.execute(user)
    }
}
