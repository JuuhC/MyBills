package com.carrati.mybills.appCompose.ui.forms.expenseIncome

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionScreen
import com.carrati.mybills.appCompose.ui.newTransaction.expense.FormExpenseIncomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class FormExpenseIncomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val oldTransaction = intent.extras?.getSerializable(EXTRA_TRANSACTION) as Transacao?
        val typeName = intent.extras?.getString(EXTRA_TYPE) ?: ""
        val type = TransactionTypeEnum.getByNome(typeName) ?: EXPENSE
        setContent {
            val viewModel = koinViewModel<FormExpenseIncomeViewModel> {
                parametersOf(oldTransaction, type)
            }
            FormTransactionScreen(
                type = type,
                viewState = viewModel.state
            ) {
                viewModel.salvarTransacao(
                    onSuccess = {
                        setResult(Activity.RESULT_OK)
                        finish()
                    },
                    onError = { errorMsg ->
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_TRANSACTION = "extraTransaction"
        const val EXTRA_TYPE = "extraType"
    }
}
