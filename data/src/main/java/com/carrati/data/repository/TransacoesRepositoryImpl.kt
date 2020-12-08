package com.carrati.data.repository

import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Response
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.ITransacoesRepository
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
            transacoesRef.whereEqualTo("conta", filter).orderBy("data", Query.Direction.DESCENDING)
        } else {
            transacoesRef
        }

        val resultList = query.get().await()

        val result =  resultList.map {
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
        result
        return result
    }
}