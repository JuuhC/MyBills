package com.carrati.mybills.appCompose.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.runtime.livedata.observeAsState
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.domain.models.TransactionTypeEnum.INCOME
import com.carrati.domain.models.TransactionTypeEnum.TRANSFER
import com.carrati.mybills.appCompose.ui.forms.expenseIncome.FormExpenseIncomeActivity
import com.carrati.mybills.appCompose.ui.forms.transfer.FormTransferActivity
import com.carrati.mybills.appCompose.ui.login.LoginActivity
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity(), FirebaseAuth.AuthStateListener {

    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        setContent {
            MdcTheme {
                MainScreen(
                    selectedDate = viewModel.selectedDate,
                    searchText = viewModel.searchText,
                    user = viewModel.user.observeAsState().value,
                    navigateToForms = { type, transaction -> navigateToForms(type, transaction) },
                    onSignOut = {
                        viewModel.signOutFirebase()
                        googleSignInClient.signOut()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAPI().getFirebaseAuth().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAPI().getFirebaseAuth().removeAuthStateListener(this)
    }

    private val startForResultForms = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.selectedDate.value = Calendar.getInstance()
        }
    }
    private fun navigateToForms(type: TransactionTypeEnum, transaction: Transacao? = null) {
        val intent = when (type) {
            EXPENSE,
            INCOME -> Intent(this, FormExpenseIncomeActivity::class.java).apply {
                this.putExtra(FormExpenseIncomeActivity.EXTRA_TYPE, type)
                this.putExtra(
                    FormExpenseIncomeActivity.EXTRA_USER_ID,
                    viewModel.user.value?.uid ?: ""
                )
                if (transaction != null) {
                    this.putExtra(FormExpenseIncomeActivity.EXTRA_TRANSACTION, transaction)
                }
            }
            TRANSFER -> Intent(this, FormTransferActivity::class.java).apply {
                this.putExtra(
                    FormExpenseIncomeActivity.EXTRA_USER_ID,
                    viewModel.user.value?.uid ?: ""
                )
            }
        }
        startForResultForms.launch(intent)
        this.onStop()
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
