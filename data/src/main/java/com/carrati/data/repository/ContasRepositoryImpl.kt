package com.carrati.data.repository

import com.carrati.data.api.FirebaseAPI
import com.carrati.domain.models.Conta
import com.carrati.domain.models.Transacao
import com.carrati.domain.repository.IContasRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ContasRepositoryImpl(api: FirebaseAPI): IContasRepository {

    //criar conta
    override suspend fun criarConta(uid: String, conta: Conta) {
        FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("contas")
            .add(conta).await()
    }

    //listar contas com nome e saldo total
    override suspend fun listarContas(uid: String): List<Conta> {
        val contasRef = FirebaseAPI().getFirebaseDb()
            .collection("users")
            .document(uid)
            .collection("contas")

        val resultList = contasRef.get().await()

        return resultList.map {
            Conta().apply {
                nome = it.getString("nome")
                saldo = it.getDouble("saldo")
            }
        }
    }
}