package com.carrati.domain.models

enum class TransactionTypeEnum(val nome: String) {
    EXPENSE(nome = "Despesa"),
    INCOME(nome = "Receita"),
    TRANSFER(nome = "Transferência")
}
