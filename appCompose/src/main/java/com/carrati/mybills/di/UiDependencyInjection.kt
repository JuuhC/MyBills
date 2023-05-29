package com.carrati.mybills.appCompose.di

import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.mybills.appCompose.ui.main.MainViewModel
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.expense.FormExpenseIncomeViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.income.FormTransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { (userId: String) ->
        HomeViewModel(get(), get(), get(), userId)
    }
    viewModel { (userId: String) ->
        TransactionsViewModel(get(), get(), userId)
    }
    viewModel { (transaction: Transacao, type: TransactionTypeEnum) ->
        FormExpenseIncomeViewModel(type, transaction, get(), get(), get(), get())
    }
    viewModel { FormTransferViewModel(get(), get(), get()) }
}
