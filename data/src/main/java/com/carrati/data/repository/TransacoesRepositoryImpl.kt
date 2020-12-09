package com.carrati.data.repository

import android.util.Log
import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

import kotlinx.coroutines.tasks.await
import kotlin.math.absoluteValue

class TransacoesRepositoryImpl(private val api: FirebaseAPI): ITransacoesRepository {

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

    override suspend fun cadastrarReceitaDespesa(uid: String, periodo: String, transacao: Transacao){
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .add(transacao).await()
    }

    override suspend fun atualizarSaldo(uid: String, periodo: String, transacao: Transacao, delete: Boolean ){
        val valor = transacao.valor!!

        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .update(transacao.tipo+"s",
                if(delete) FieldValue.increment(valor.unaryMinus())
                else FieldValue.increment(valor)
            ).await()

        val saldoConta = if (transacao.tipo == "receita") {
            if(delete) FieldValue.increment(valor.unaryMinus())
            else FieldValue.increment(valor)
        } else {
            if(delete) FieldValue.increment(valor)
            else FieldValue.increment(valor.unaryMinus())
        }
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("contas")
            .document(transacao.conta!!)
            .update("saldo", saldoConta).await()
    }

    override suspend fun obterBalancoMensal(uid: String, periodo: String): HashMap<String, Double>{
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

    override suspend fun deletarTransacao(uid: String, periodo: String, transacao: Transacao){
        api.getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .document(transacao.id!!)
            .delete().await()
    }
}