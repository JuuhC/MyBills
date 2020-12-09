package com.carrati.domain.usecases.contas

import com.carrati.domain.models.Conta
import com.carrati.domain.repository.IContasRepository

class ListarContasUC(private val repo: IContasRepository) {

    suspend fun execute(uid: String): List<Conta> = repo.listarContas(uid)
}