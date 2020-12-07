package com.carrati.domain.di

import com.carrati.domain.usecases.usuarios.ChecarUsuarioAutenticadoUC
import com.carrati.domain.usecases.usuarios.CriarUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.ObterUsuarioFirestoreUC
import com.carrati.domain.usecases.usuarios.SignInWithGoogleUC
import org.koin.dsl.module

val domainModule = module {
    factory { ChecarUsuarioAutenticadoUC(get()) }
    factory { CriarUsuarioFirestoreUC(get()) }
    factory { ObterUsuarioFirestoreUC(get()) }
    factory { SignInWithGoogleUC(get()) }
}