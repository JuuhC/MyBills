package com.carrati.mybills.appCompose.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carrati.mybills.appCompose.ui.login.LoginActivity
import com.carrati.mybills.appCompose.ui.main.MainActivity
import com.google.accompanist.themeadapter.material.MdcTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MdcTheme {
                SplashScreen()
            }
        }
        lifecycleScope.launch {
            delay(5000)
            checkIfUserIsAuthenticated()
        }
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this) { user ->
            val intent = if (user.isAuthenticated) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
