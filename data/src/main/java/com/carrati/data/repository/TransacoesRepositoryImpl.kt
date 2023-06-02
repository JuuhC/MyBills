package com.carrati.data.repository

import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class TransacoesRepositoryImpl(private val api: FirebaseAPI) : ITransacoesRepository {

    override suspend fun getTransacoes(uid: String, periodo: String): List<Transacao> {
        val transacoesRef = api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .orderBy("data", Query.Direction.DESCENDING)

        val resultList = transacoesRef.get().await()

        return resultList.map {
            Transacao().apply {
                id = it.id
                tipo = it.getString("tipo")
                data = it.getString("data")
                nome = it.getString("nome")
                valor = it.getDouble("valor")
                conta = it.getString("conta")
                efetuado = it.getBoolean("efetuado")
            }
        }
    }

    override suspend fun cadastrarReceitaDespesa(uid: String, periodo: String, transacao: Transacao) {
        val docPeriodPath = api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)

        val docPeriod = docPeriodPath.get().await()

        if (docPeriod.exists().not()) {
            docPeriodPath.set(
                hashMapOf(
                    "receitas" to 0.0,
                    "despesas" to 0.0
                )
            )
        }

        docPeriodPath.collection("transacoes").add(transacao).await()
    }

    override suspend fun atualizarSaldo(
        uid: String,
        periodo: String,
        transacao: Transacao,
        transacaoAntiga: Transacao?,
        delete: Boolean,
        transferencia: Boolean
    ) {
        val valor = transacao.valor!!

        // atualizar balança - receitas e despesas do mes
        if (!transferencia) {
            api.getFirebaseDb()
                .collection("users")
                .document(uid)
                .collection("periodos")
                .document(periodo)
                .update(
                    transacao.tipo + "s",
                    when {
                        delete -> FieldValue.increment(valor.unaryMinus())
                        transacaoAntiga != null && transacaoAntiga.efetuado!! -> FieldValue.increment(
                            valor - transacaoAntiga.valor!!
                        )
                        else -> FieldValue.increment(valor)
                    }
                ).await()
        }

        // atualizar saldo da conta
        val saldoConta = if (transacao.tipo == "receita") {
            when {
                delete -> FieldValue.increment(valor.unaryMinus())
                transacaoAntiga?.valor != null && transacaoAntiga.efetuado!! -> FieldValue.increment(
                    valor - transacaoAntiga.valor!!
                )
                else -> FieldValue.increment(valor)
            }
        } else {
            when {
                delete -> FieldValue.increment(valor)
                transacaoAntiga?.valor != null && transacaoAntiga.efetuado!! -> FieldValue.increment(
                    (valor - transacaoAntiga.valor!!).unaryMinus()
                )
                else -> FieldValue.increment(valor.unaryMinus())
            }
        }
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("contas")
            .document(transacao.conta!!)
            .update("saldo", saldoConta).await()
    }

    override suspend fun obterBalancoMensal(uid: String, periodo: String): HashMap<String, Double> {
        val result = api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .get().await()

        return hashMapOf(
            "receitas" to (result.getDouble("receitas") ?: 0.0),
            "despesas" to (result.getDouble("despesas") ?: 0.0)
        )
    }

    override suspend fun deletarTransacao(uid: String, periodo: String, transacao: Transacao) {
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .document(transacao.id!!)
            .delete().await()
    }

    override suspend fun editarTransacao(uid: String, periodo: String, transacao: Transacao) {
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .document(transacao.id!!)
            .set(transacao).await()
    }
}
