package com.carrati.mybills.appCompose.di

import com.carrati.mybills.appCompose.ui.main.MainViewModel
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.expense.ExpenseViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.income.IncomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { (userId: String) ->
        HomeViewModel(get(), get(), get(), userId)
    }
    viewModel { TransactionsViewModel(/*get(), get(), get()*/) }

    //viewModel { ExpenseViewModel(/*get(), get(), get(), get()*/) }
    //viewModel { IncomeViewModel(/*get(), get(), get(), get()*/) }
    // viewModel { TransferenciaViewModel(get(), get(), get()) }
}
