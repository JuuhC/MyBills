package com.carrati.data.repository

import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

import kotlinx.coroutines.tasks.await

class TransacoesRepositoryImpl(api: FirebaseAPI): ITransacoesRepository {

    //criar receita

    //criar despesa(nome, categoria, valor, contaId, data)

    //criar transferencia de para (conta1, conta2, valor, data)
        //call criar despesa ("transferencia para conta2", "tranferencia", valor, contaId)
        //call criar receita ("transferencia de conta1", "tranferencia", valor, contaId)

    //listar transacoes
    override suspend fun getTransacoes(uid: String, periodo: String, filter: String?): List<Transacao> {
        val transacoesRef = FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")

        val query = if(!filter.isNullOrEmpty()){
            transacoesRef.whereEqualTo("conta", filter)
        } else {
            transacoesRef
        }
        query.orderBy("data", Query.Direction.DESCENDING)

        val resultList = query.get().await()

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
        FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("periodos")
            .document(periodo)
            .collection("transacoes")
            .add(transacao).await()
    }

    override suspend fun atualizarSaldo(uid: String, periodo: String, transacao: Transacao ){
        FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .update(transacao.tipo+"s", FieldValue.increment(transacao.valor!!)).await()

        val saldoConta = if (transacao.tipo == "receita") {
            FieldValue.increment(transacao.valor!!)
        } else {
            FieldValue.increment(transacao.valor!!.unaryMinus())
        }
        FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("contas")
            .document(transacao.conta!!)
            .update("saldo", saldoConta).await()
    }
}