package com.carrati.mybills.appCompose.di

import com.carrati.domain.models.Transacao
import com.carrati.mybills.appCompose.ui.main.MainViewModel
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.transactions.TransactionsViewModel
import com.carrati.mybills.appCompose.ui.newTransaction.expense.ExpenseViewModel
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

    // viewModel { IncomeViewModel(/*get(), get(), get(), get()*/) }
    // viewModel { TransferenciaViewModel(get(), get(), get()) }
    viewModel { (userId: String, transaction: Transacao) ->
        ExpenseViewModel(userId, transaction, get(), get(), get())
    }
}
