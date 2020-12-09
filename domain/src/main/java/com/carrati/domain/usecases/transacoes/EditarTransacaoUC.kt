package com.carrati.domain.usecases.transacoes

import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository

class EditarTransacaoUC(private val repo: ITransacoesRepository) {

    suspend fun execute(uid: String, period: String, transacao: Transacao, transacaoAntiga: Transacao? = null) {
        repo.editarTransacao(uid,period,transacao)
        if(transacao.efetuado != null && transacao.efetuado!!) {
            repo.atualizarSaldo(uid,period,transacao,transacaoAntiga)
        }
    }
}