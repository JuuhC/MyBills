package com.carrati.mybills.ui.transacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransacoesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is transações Fragment"
    }
    val text: LiveData<String> = _text
}