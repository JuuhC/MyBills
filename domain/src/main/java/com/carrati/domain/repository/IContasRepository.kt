package com.carrati.domain.repository

import com.carrati.domain.models.Conta

interface IContasRepository {
    suspend fun criarConta(uid: String, conta: Conta)
    suspend fun listarContas(uid: String): List<Conta>
}