package com.carrati.mybills.appCompose.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.mybills.appCompose.ui.login.LoginActivity
import com.carrati.mybills.appCompose.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            SplashScreen()
        }
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this) { user ->
            val intent = if (user.isAuthenticated) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
