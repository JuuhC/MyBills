package com.carrati.mybills.appCompose.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrati.domain.models.Usuario
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignOutFirebaseUC
import com.carrati.mybills.appCompose.extensions.startDay
import java.util.*

class MainViewModel(
    private val signOutFirebaseUC: SignOutFirebaseUC,
    private val obterUsuarioFirestoreUC: ObterUsuarioFirestoreUC
) : ViewModel() {
    val selectedDate: MutableState<Calendar> = mutableStateOf(
        Calendar.getInstance().startDay()
    )
    val searchText: MutableState<String> = mutableStateOf("")

    private var _user: MutableLiveData<Usuario> = obterUsuarioFirestoreUC.execute()
    var user: LiveData<Usuario> = _user

    fun signOutFirebase() {
        signOutFirebaseUC.execute()
    }
}
