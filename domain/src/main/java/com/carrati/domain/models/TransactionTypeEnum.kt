package com.carrati.domain.models

import java.io.Serializable

enum class TransactionTypeEnum(val nome: String) : Serializable {
    EXPENSE(nome = "Despesa"),
    INCOME(nome = "Receita"),
    TRANSFER(nome = "Transferência");

    companion object {
        fun getByNome(nome: String): TransactionTypeEnum? {
            return values().firstOrNull { it.nome.equals(nome, ignoreCase = true) }
        }
    }
}
