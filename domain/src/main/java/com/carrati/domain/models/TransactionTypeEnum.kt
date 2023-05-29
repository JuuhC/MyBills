package com.carrati.domain.models

enum class TransactionTypeEnum(val nome: String) {
    EXPENSE(nome = "Despesa"),
    INCOME(nome = "Receita"),
    TRANSFER(nome = "Transferência");

    companion object {
        fun getByNome(nome: String): TransactionTypeEnum? {
            return values().firstOrNull { it.nome.contains(nome, ignoreCase = true) }
        }
    }
}
