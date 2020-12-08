package com.carrati.data.repository

import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Conta
import com.carrati.domain.repository.IContasRepository

class ContasRepositoryImpl(api: FirebaseAPI): IContasRepository {

    //criar conta
    fun criarConta(conta: Conta) {

    }

    //listar contas com nome e saldo total
    fun listarContas(): List<Conta> {
        return listOf()
    }
}