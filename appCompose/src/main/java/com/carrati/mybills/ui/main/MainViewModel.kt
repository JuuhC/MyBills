package com.carrati.mybills.appCompose.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    val selectedDate: MutableState<Calendar> = mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
    )
}
