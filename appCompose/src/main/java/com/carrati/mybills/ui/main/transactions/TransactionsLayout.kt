package com.carrati.mybills.appCompose.ui.main.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.domain.models.Transacao
import com.carrati.mybills.appCompose.R.drawable
import com.carrati.mybills.appCompose.ui.newTransaction.TransactionTypeEnum

@Preview
@Composable
private fun TransactionsScreenPreview() {
    val state by remember { mutableStateOf(TransactionsViewState()) }
    state.transactionsByMonth = listOf(
        Transacao(),
        Transacao().apply { efetuado = true },
        Transacao(),
        Transacao().apply { efetuado = true },
        Transacao()
    )
    TransactionsScreen(state = state)
}

@Composable
fun TransactionsScreen(state: TransactionsViewState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF3FB))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(60.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.size(4.dp))
                }
                items(state.transactionsByMonth) { transaction ->
                    TransactionItem(transaction = transaction)
                }
                item {
                    Spacer(modifier = Modifier.size(4.dp))
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transacao
) {
    val darkColor = if (transaction.tipo == TransactionTypeEnum.EXPENSE.name) {
        Color(0xFF118336)
    } else {
        Color(0xFFED4588)
    }

    val lightColor = if (transaction.tipo == TransactionTypeEnum.EXPENSE.name) {
        Color(0xFF2AA653)
    } else {
        Color(0xFFF866A1)
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = drawable.ic_despesa_24dp),
                tint = darkColor,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f).wrapContentHeight()
            ) {
                Text(
                    text = transaction.nome ?: "Nome",
                    color = darkColor,
                    fontSize = 15.sp
                )
                Text(
                    text = transaction.conta ?: "Conta",
                    color = lightColor
                )
            }
            Column(
                modifier = Modifier.weight(1f).wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = transaction.valor?.toString() ?: "R$ 0,00",
                    color = darkColor,
                    fontSize = 15.sp
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = transaction.data ?: "0000/00/00",
                    color = lightColor
                )
            }
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (transaction.efetuado == true) Color(0xFFFAC853) else Color.LightGray
            )
        }
    }
}
