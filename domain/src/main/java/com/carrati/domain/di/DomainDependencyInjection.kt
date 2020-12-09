package com.carrati.domain.di

import com.carrati.domain.usecases.contas.CriarContaUC
import com.carrati.domain.usecases.contas.ListarContasUC
import com.carrati.domain.usecases.transacoes.*
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
    factory { ObterBalancoMensalUC(get()) }
    factory { ListarContasUC(get()) }
    factory { CriarContaUC(get()) }
    factory { DeletarTransacaoUC(get()) }
}