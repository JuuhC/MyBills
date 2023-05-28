package com.carrati.mybills.appCompose.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.domain.models.TransactionTypeEnum.INCOME
import com.carrati.domain.models.TransactionTypeEnum.TRANSFER
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            MainScreen(
                selectedDate = viewModel.selectedDate,
                searchText = viewModel.searchText,
                user = viewModel.user,
                navigateToForms = { type -> navigateToForms(type) },
                onSignOut = { viewModel.signOutFirebase() }
            )
        }
    }

    private fun navigateToForms(type: TransactionTypeEnum) {
        when (type) {
            EXPENSE -> {}
            INCOME -> {}
            TRANSFER -> {}
        }
    }
}
