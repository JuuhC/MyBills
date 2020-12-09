package com.carrati.mybills.di

import com.carrati.mybills.ui.despesa.DespesaViewModel
import com.carrati.mybills.ui.home.HomeViewModel
import com.carrati.mybills.ui.login.LoginViewModel
import com.carrati.mybills.ui.receita.ReceitaViewModel
import com.carrati.mybills.ui.transacoes.TransacoesViewModel
import com.carrati.mybills.ui.transferencia.TransferenciaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { TransacoesViewModel(get(), get(), get()) }

    viewModel { DespesaViewModel(get(), get(), get()) }
    viewModel { ReceitaViewModel(get(), get(), get()) }
    viewModel { TransferenciaViewModel(get(), get(), get()) }
}