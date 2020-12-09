package com.carrati.domain.usecases.transacoes

import com.carrati.domain.repository.ITransacoesRepository

class ObterBalancoMensalUC(private val repo: ITransacoesRepository) {

    suspend fun execute(uid: String, periodo: String): HashMap<String, Double> = repo.obterBalancoMensal(uid, periodo)
}