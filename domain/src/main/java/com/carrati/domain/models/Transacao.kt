package com.carrati.domain.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class Transacao: Serializable {
    @get:Exclude var id: String? = null
    var tipo: String? = null
    var data: String? = null
    var nome: String? = null
    var valor: Double? = null
    var conta: String? = null
    var efetuado: Boolean? = false
}