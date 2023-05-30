package com.carrati.mybills.appCompose.ui.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.usuarios.CriarUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignInWithGoogleUC
import com.google.firebase.auth.AuthCredential

class LoginViewModel(
    private val criarUsuarioFirestoreUC: CriarUsuarioFirestoreUC,
    private val signInWithGoogleUC: SignInWithGoogleUC
) : ViewModel() {

    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    var authenticatedUserLiveData: LiveData<Usuario>? = null
    var createdUserLiveData: LiveData<Usuario>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = signInWithGoogleUC.execute(googleAuthCredential)
    }

    fun createUserFirestore(user: Usuario) {
        createdUserLiveData = criarUsuarioFirestoreUC.execute(user)
    }
}
