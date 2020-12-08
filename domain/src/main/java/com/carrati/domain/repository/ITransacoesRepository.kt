package com.carrati.domain.repository

import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao

interface ITransacoesRepository {
    suspend fun getTransacoes(uid: String, periodo: String, filter: String?): List<Transacao>
    suspend fun cadastrarReceitaDespesa(uid: String, periodo: String, transacao: Transacao )
    suspend fun atualizarSaldo(uid: String, periodo: String, transacao: Transacao )
}