package com.carrati.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Usuario
import com.carrati.domain.repository.IUsuariosRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot

class UsuariosRepositoryImpl(api: FirebaseAPI): IUsuariosRepository {
    private val firebaseAuth = api.getFirebaseAuth()
    private val rootRef = api.getFirebaseDb()
    private val usersRef = rootRef.collection("users")

    override fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<Usuario>? {
        val isUserAuthenticatedMutableLiveData: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
        val firebaseUser = firebaseAuth.currentUser
        val user = Usuario()
        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticatedMutableLiveData.setValue(user)
        } else {
            user.apply {
                this.uid = firebaseUser.uid
                this.name = firebaseUser.displayName
                this.email = firebaseUser.email
                this.photoUrl = firebaseUser.photoUrl.toString()
                this.isAuthenticated = true
            }
            isUserAuthenticatedMutableLiveData.setValue(user)
        }
        return isUserAuthenticatedMutableLiveData
    }

    override fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<Usuario>? {
        val authenticatedUserMutableLiveData: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!).addOnCompleteListener { authTask: Task<AuthResult> ->
            if (authTask.isSuccessful) {
                val isNewUser = authTask.result!!.additionalUserInfo!!.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    val user = Usuario().apply {
                        this.uid = firebaseUser.uid
                        this.name = firebaseUser.displayName
                        this.email = firebaseUser.email
                        this.photoUrl = firebaseUser.photoUrl.toString()
                        this.isNew = isNewUser
                    }
                    authenticatedUserMutableLiveData.postValue(user)
                }
            } else {
                Log.e("", authTask.exception?.message.toString())
            }
        }
        return authenticatedUserMutableLiveData
    }

    override fun createUserInFirestoreIfNotExists(authenticatedUser: Usuario): MutableLiveData<Usuario>? {
        val newUserMutableLiveData: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
        val uidRef = usersRef.document(authenticatedUser.uid!!)
        uidRef.get().addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result
                if (!document!!.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener { userCreationTask: Task<Void?> ->
                        if (userCreationTask.isSuccessful) {
                            authenticatedUser.isCreated = true
                            newUserMutableLiveData.setValue(authenticatedUser)
                        } else {
                            Log.e("", userCreationTask.exception?.message.toString())
                        }
                    }
                } else {
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
            } else {
                Log.e("", uidTask.exception?.message.toString())
            }
        }
        return newUserMutableLiveData
    }

    override fun getUserFromFirestore(uid: String?): MutableLiveData<Usuario?> {
        val userMutableLiveData: MutableLiveData<Usuario?> = MutableLiveData<Usuario?>()
        usersRef.document(uid!!).get().addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
            if (userTask.isSuccessful) {
                val document = userTask.result
                if (document!!.exists()) {
                    val user: Usuario? = document.toObject(Usuario::class.java)
                    userMutableLiveData.postValue(user)
                }
            } else {
                Log.e("", userTask.exception?.message.toString())
            }
        }
        return userMutableLiveData
    }

    override fun signOutFirebase() {
        firebaseAuth.signOut()
    }
}