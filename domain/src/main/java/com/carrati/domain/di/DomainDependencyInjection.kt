package com.carrati.domain.di

import com.carrati.domain.usecases.usuarios.*
import org.koin.dsl.module

val domainModule = module {
    factory { ChecarUsuarioAutenticadoUC(get()) }
    factory { CriarUsuarioFirestoreUC(get()) }
    factory { ObterUsuarioFirestoreUC(get()) }
    factory { SignInWithGoogleUC(get()) }
    factory { SignOutFirebaseUC(get()) }
}