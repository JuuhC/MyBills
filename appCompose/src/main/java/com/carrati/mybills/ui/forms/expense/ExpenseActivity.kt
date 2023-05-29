package com.carrati.mybills.ui.forms.expense

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionScreen
import com.carrati.mybills.appCompose.ui.newTransaction.expense.ExpenseViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ExpenseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val oldTransaction = intent.extras?.getSerializable(EXTRA_TRANSACTION) as Transacao?
        val userId = intent.extras?.getInt(EXTRA_UID)
        setContent {
            val viewModel = koinViewModel<ExpenseViewModel> {
                parametersOf(oldTransaction, userId)
            }
            FormTransactionScreen(type = EXPENSE, viewState = viewModel.state) {
                viewModel.salvarTransacao {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_UID = "extraUserId"
        const val EXTRA_TRANSACTION = "extraTransaction"
    }
}
