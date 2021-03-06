package com.carrati.domain.repository

import com.carrati.domain.models.Transacao

interface ITransacoesRepository {
    suspend fun getTransacoes(uid: String, periodo: String): List<Transacao>
    suspend fun cadastrarReceitaDespesa(uid: String, periodo: String, transacao: Transacao )
    suspend fun obterBalancoMensal(uid: String, periodo: String): HashMap<String, Double>
    suspend fun deletarTransacao(uid: String, periodo: String, transacao: Transacao)
    suspend fun editarTransacao(uid: String, periodo: String, transacao: Transacao )
    suspend fun atualizarSaldo(uid: String, periodo: String, transacao: Transacao,
                               transacaoAntiga: Transacao? = null, delete: Boolean = false,
                               transferencia: Boolean = false )
}