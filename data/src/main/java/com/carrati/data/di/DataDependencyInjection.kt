package com.carrati.data.di

import org.koin.dsl.module

val apiModule = module {

}

val repositoryModule = module {

}

val dataModule = listOf(apiModule, repositoryModule)