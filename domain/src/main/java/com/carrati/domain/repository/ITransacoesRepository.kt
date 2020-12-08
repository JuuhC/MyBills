package com.carrati.domain.repository

import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao

interface ITransacoesRepository {
    suspend fun getTransacoes(uid: String, periodo: String, filter: String?): List<Transacao>
}