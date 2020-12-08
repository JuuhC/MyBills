package com.carrati.domain.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class Usuario: Serializable {
    var uid: String? = null
    var name: String? = null
    var email: String? = null
    var photoUrl: String? = null
    var despesas: Double = 0.0
    var receitas: Double = 0.0

    @get:Exclude var isAuthenticated = false
    @get:Exclude var isNew = false
    @get:Exclude var isCreated = false
}