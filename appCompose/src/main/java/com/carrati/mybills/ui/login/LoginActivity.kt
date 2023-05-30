package com.carrati.mybills.appCompose.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.carrati.domain.models.Usuario
import com.carrati.mybills.appCompose.R
import com.carrati.mybills.appCompose.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        setContent {
            LoginScreen(viewModel.isLoading) {
                signIn()
            }
        }
    }

    private fun signIn() {
        viewModel.isLoading.value = true
        val signInIntent = googleSignInClient.signInIntent
        startForResultSignIn.launch(signInIntent)
    }

    private val startForResultSignIn = registerForActivityResult(StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val googleSignInAccount = task.getResult(ApiException::class.java)!!
            googleSignInAccount.let {
                val googleTokenId = googleSignInAccount.idToken
                val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                viewModel.signInWithGoogle(googleAuthCredential)
                viewModel.authenticatedUserLiveData?.observe(this) { authenticatedUser ->
                    criarUserFirestore(authenticatedUser)
                }
            }
        } catch (e: ApiException) {
            viewModel.isLoading.value = false
            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_LONG).show()
        }
    }

    private fun criarUserFirestore(user: Usuario) {
        viewModel.createUserFirestore(user)
        viewModel.createdUserLiveData?.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
