package com.carrati.domain.usecases.transacoes

import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository

class DeletarTransacaoUC(private var repo: ITransacoesRepository) {
    suspend fun execute(uid: String, periodo: String, transacao: Transacao){
        repo.deletarTransacao(uid, periodo, transacao)
        repo.atualizarSaldo(uid, periodo, transacao, true)
    }
}