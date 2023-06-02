package com.carrati.mybills.appCompose.di

import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.mybills.appCompose.ui.login.LoginViewModel
import com.carrati.mybills.appCompose.ui.main.MainViewModel
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.expense.FormExpenseIncomeViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.income.FormTransferViewModel
import com.carrati.mybills.appCompose.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { TransactionsViewModel(get(), get()) }
    viewModel { (userId: String) ->
        FormTransferViewModel(userId, get(), get())
    }
    viewModel { (transaction: Transacao, type: TransactionTypeEnum, userId: String) ->
        FormExpenseIncomeViewModel(type, transaction, userId, get(), get(), get())
    }
}
