package com.carrati.mybills.di

import com.carrati.mybills.ui.home.HomeViewModel
import com.carrati.mybills.ui.login.LoginViewModel
import com.carrati.mybills.ui.transacoes.TransacoesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { HomeViewModel() }
    viewModel { TransacoesViewModel() }
}