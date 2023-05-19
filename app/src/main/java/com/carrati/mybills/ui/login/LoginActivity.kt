package com.carrati.mybills.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.carrati.domain.models.Usuario
import com.carrati.mybills.app.R
import com.carrati.mybills.app.databinding.ActivityLoginBinding
import com.carrati.mybills.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        checkIfUserIsAuthenticated()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.googleSignInButton.setOnClickListener { signIn() }
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this) { user ->
            if (user.isAuthenticated) {
                obterUserFirestore(user)
            } else {
                binding.clLoading.isVisible = false
                signIn()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        binding.clLoading.isVisible = true
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(ApiException::class.java)!!
                googleSignInAccount.let {
                    val googleTokenId = googleSignInAccount.idToken
                    val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                    viewModel.signInWithGoogle(googleAuthCredential)
                    viewModel.authenticatedUserLiveData?.observe(this) { authenticatedUser ->
                        obterUserFirestore(authenticatedUser)
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_LONG).show()
                binding.clLoading.isVisible = false
            }
        }
    }

    private fun obterUserFirestore(user: Usuario) {
        viewModel.createUserFirestore(user)
        viewModel.createdUserLiveData?.observe(this) { createdUser ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", createdUser)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
