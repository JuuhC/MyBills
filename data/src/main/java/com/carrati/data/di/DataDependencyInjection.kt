package com.carrati.data.di

import com.carrati.data.api.FirebaseAPI
import org.koin.dsl.module

val apiModule = module {
    single { FirebaseAPI() }
}

val repositoryModule = module {

}

val dataModule = listOf(apiModule, repositoryModule)