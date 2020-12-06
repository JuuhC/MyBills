package com.carrati.data.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAPI {
    companion object {
        private var auth: FirebaseAuth? = null
        private var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        private var firestore: FirebaseFirestore? = null
    }

    fun getFirebaseAuth(): FirebaseAuth {
        if (auth == null) auth = FirebaseAuth.getInstance()
        return auth!!
    }

    fun getFirebaseDb(): FirebaseFirestore {
        if (firestore == null) firestore = FirebaseFirestore.getInstance()
        return firestore!!
    }

    fun sendThrowableToFirebase(t: Throwable) {
        crashlytics.recordException(t)
        crashlytics.sendUnsentReports()
    }
}