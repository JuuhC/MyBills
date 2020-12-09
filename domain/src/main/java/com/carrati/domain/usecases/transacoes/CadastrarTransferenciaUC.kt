package com.carrati.domain.usecases.transacoes

import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository

class CadastrarTransferenciaUC(private val repo: ITransacoesRepository) {

    suspend fun execute(
        uid: String,
        period: String,
        transacao1: Transacao,
        transacao2: Transacao
    ) {
        repo.cadastrarReceitaDespesa(uid, period, transacao1)
        repo.cadastrarReceitaDespesa(uid, period, transacao2)

        if(transacao1.efetuado != null && transacao1.efetuado!!) {
            repo.atualizarSaldo(uid,period,transacao1,transferencia = true)
        }

        if(transacao2.efetuado != null && transacao2.efetuado!!) {
            repo.atualizarSaldo(uid,period,transacao2,transferencia = true)
        }
    }
}