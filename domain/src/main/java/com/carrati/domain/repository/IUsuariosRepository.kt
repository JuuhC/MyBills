package com.carrati.domain.repository

import androidx.lifecycle.MutableLiveData
import com.carrati.domain.models.Usuario
import com.google.firebase.auth.AuthCredential

interface IUsuariosRepository {
    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<Usuario>?
    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<Usuario>?
    fun createUserInFirestoreIfNotExists(authenticatedUser: Usuario): MutableLiveData<Usuario>?
    fun getUserFromFirestore(uid: String?): MutableLiveData<Usuario?>
}