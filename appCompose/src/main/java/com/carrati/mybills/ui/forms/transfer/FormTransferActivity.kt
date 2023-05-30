package com.carrati.mybills.appCompose.ui.forms.transfer

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.TransactionTypeEnum.TRANSFER
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionScreen
import com.carrati.mybills.appCompose.ui.newTransaction.income.FormTransferViewModel
import org.koin.androidx.compose.koinViewModel

class FormTransferActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            val viewModel = koinViewModel<FormTransferViewModel>()
            FormTransactionScreen(
                type = TRANSFER,
                viewState = viewModel.state
            ) {
                viewModel.salvarTransferencia {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
}
