package com.carrati.mybills.app.di

import com.carrati.mybills.app.ui.despesa.DespesaViewModel
import com.carrati.mybills.app.ui.home.HomeViewModel
import com.carrati.mybills.app.ui.login.LoginViewModel
import com.carrati.mybills.app.ui.receita.ReceitaViewModel
import com.carrati.mybills.app.ui.transacoes.TransacoesViewModel
import com.carrati.mybills.app.ui.transferencia.TransferenciaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { TransacoesViewModel(get(), get(), get()) }

    viewModel { DespesaViewModel(get(), get(), get(), get()) }
    viewModel { ReceitaViewModel(get(), get(), get(), get()) }
    viewModel { TransferenciaViewModel(get(), get(), get()) }
}