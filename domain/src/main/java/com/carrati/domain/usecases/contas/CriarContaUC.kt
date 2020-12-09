package com.carrati.domain.usecases.contas

import com.carrati.domain.models.Conta
import com.carrati.domain.repository.IContasRepository

class CriarContaUC(private val repo: IContasRepository) {

    suspend fun execute(uid: String, conta: Conta){ repo.criarConta(uid, conta) }
}