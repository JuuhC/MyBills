package com.carrati.domain.usecases.transacoes

import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository

class ListarTransacoesUC(private val repo: ITransacoesRepository) {

    suspend fun execute(
        uid: String,
        period: String
    ): List<Transacao> = repo.getTransacoes(uid,period)
}