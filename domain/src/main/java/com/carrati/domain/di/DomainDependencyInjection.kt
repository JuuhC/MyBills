package com.carrati.domain.di

import com.carrati.domain.usecases.transacoes.CadastrarReceitaDespesaUC
import com.carrati.domain.usecases.transacoes.CadastrarTransferenciaUC
import com.carrati.domain.usecases.transacoes.ListarTransacoesUC
import com.carrati.domain.usecases.usuarios.*
import org.koin.dsl.module

val domainModule = module {
    factory { ChecarUsuarioAutenticadoUC(get()) }
    factory { CriarUsuarioFirestoreUC(get()) }
    factory { ObterUsuarioFirestoreUC(get()) }
    factory { SignInWithGoogleUC(get()) }
    factory { SignOutFirebaseUC(get()) }
    factory { ListarTransacoesUC(get()) }
    factory { CadastrarReceitaDespesaUC(get()) }
    factory { CadastrarTransferenciaUC(get()) }
}