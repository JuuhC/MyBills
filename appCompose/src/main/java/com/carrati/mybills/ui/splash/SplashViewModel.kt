package com.carrati.mybills.appCompose.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.usuarios.ChecarUsuarioAutenticadoUC

class SplashViewModel(
    private val checarUsuarioAutenticadoUC: ChecarUsuarioAutenticadoUC
) : ViewModel() {
    var isUserAuthenticatedLiveData: LiveData<Usuario>? = null
    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = checarUsuarioAutenticadoUC.execute()
    }
}
