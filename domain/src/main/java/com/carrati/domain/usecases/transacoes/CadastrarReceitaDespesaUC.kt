package com.carrati.domain.usecases.transacoes

import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository

class CadastrarReceitaDespesaUC(private val repo: ITransacoesRepository) {

    suspend fun execute(
        uid: String,
        period: String,
        transacao: Transacao
    ) {
        repo.cadastrarReceitaDespesa(uid,period,transacao)
        if(transacao.efetuado != null && transacao.efetuado!!) {
            repo.atualizarSaldo(uid,period,transacao)
        }
    }
}