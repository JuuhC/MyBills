package com.carrati.mybills.appCompose.ui.forms.expenseIncome

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.mybills.appCompose.R
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionScreen
import com.carrati.mybills.appCompose.ui.newTransaction.expense.FormExpenseIncomeViewModel
import com.google.accompanist.themeadapter.material.MdcTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class FormExpenseIncomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val oldTransaction = intent.extras?.getSerializable(EXTRA_TRANSACTION) as Transacao?
        val type = intent.extras?.getSerializable(EXTRA_TYPE) as TransactionTypeEnum? ?: EXPENSE
        val userId = intent.extras?.getString(EXTRA_USER_ID) ?: ""

        val style = if (type == EXPENSE) R.style.DespesaTheme else R.style.ReceitaTheme
        theme.applyStyle(style, true)

        setContent {
            val viewModel = koinViewModel<FormExpenseIncomeViewModel> {
                parametersOf(oldTransaction, type, userId)
            }
            MdcTheme {
                FormTransactionScreen(
                    type = type,
                    viewState = viewModel.state
                ) {
                    viewModel.salvarTransacao(
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(
                                    this@FormExpenseIncomeActivity,
                                    "Transação salva.",
                                    Toast.LENGTH_LONG
                                ).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        },
                        onError = { errorMsg ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@FormExpenseIncomeActivity,
                                    errorMsg,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_TRANSACTION = "extraTransaction"
        const val EXTRA_TYPE = "extraType"
        const val EXTRA_USER_ID = "extraUserId"
    }
}
