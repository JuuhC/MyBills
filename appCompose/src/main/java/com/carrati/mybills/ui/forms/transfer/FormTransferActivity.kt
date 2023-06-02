package com.carrati.mybills.appCompose.ui.forms.transfer

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.TransactionTypeEnum.TRANSFER
import com.carrati.mybills.appCompose.ui.forms.expenseIncome.FormExpenseIncomeActivity
import com.carrati.mybills.appCompose.ui.newTransaction.FormTransactionScreen
import com.carrati.mybills.appCompose.ui.newTransaction.income.FormTransferViewModel
import com.google.accompanist.themeadapter.material.MdcTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class FormTransferActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.extras?.getString(FormExpenseIncomeActivity.EXTRA_USER_ID) ?: ""
        setContent {
            val viewModel = koinViewModel<FormTransferViewModel> { parametersOf(userId) }
            MdcTheme {
                FormTransactionScreen(type = TRANSFER, viewState = viewModel.state) {
                    viewModel.salvarTransferencia(
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(
                                    this@FormTransferActivity,
                                    "Transferência salva.",
                                    Toast.LENGTH_LONG
                                ).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        },
                        onError = { errorMsg ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@FormTransferActivity,
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
}
