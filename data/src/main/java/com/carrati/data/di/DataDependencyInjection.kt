package com.carrati.data.di

import com.carrati.data.api.FirebaseAPI
import com.carrati.data.repository.ContasRepositoryImpl
import com.carrati.data.repository.TransacoesRepositoryImpl
import com.carrati.data.repository.UsuariosRepositoryImpl
import com.carrati.domain.repository.IContasRepository
import com.carrati.domain.repository.ITransacoesRepository
import com.carrati.domain.repository.IUsuariosRepository
import org.koin.dsl.module

val apiModule = module {
    single { FirebaseAPI() }
}

val repositoryModule = module {
    factory<IUsuariosRepository> { UsuariosRepositoryImpl(get()) }
    factory<ITransacoesRepository> { TransacoesRepositoryImpl(get()) }
    factory<IContasRepository> { ContasRepositoryImpl(get()) }
}

val dataModule = listOf(apiModule, repositoryModule)