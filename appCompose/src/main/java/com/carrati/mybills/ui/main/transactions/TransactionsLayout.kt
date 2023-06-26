package com.carrati.mybills.appCompose.ui.main.transactions

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.domain.models.Transacao
import com.carrati.domain.models.TransactionTypeEnum
import com.carrati.domain.models.TransactionTypeEnum.EXPENSE
import com.carrati.mybills.appCompose.R
import com.carrati.mybills.appCompose.ui.common.EmptyListCardLayout
import com.carrati.mybills.appCompose.ui.common.ErrorMessageLayout
import com.carrati.mybills.extensions.toMoneyString
import java.util.*
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun TransactionsScreenPreview() {
    val state by remember { mutableStateOf(TransactionsViewState()) }
    state.transactionsAll = listOf(
        Transacao(),
        Transacao().apply {
            tipo = "despesa"
            efetuado = true
        },
        Transacao(),
        Transacao().apply {
            tipo = "despesa"
            efetuado = true
        },
        Transacao()
    )
    state.isLoading = false
    state.transactionsFiltered = state.transactionsAll
    TransactionsLayout(
        state = state,
        onDeleteTransaction = {},
        onRefreshData = {},
        onItemClick = { _, _ -> }
    )
}

@Composable
fun TransactionsScreen(
    selectedDate: Calendar,
    userId: String,
    searchText: String,
    navigateToForms: (TransactionTypeEnum, Transacao) -> Unit
) {
    val viewModel: TransactionsViewModel = koinViewModel()

    LaunchedEffect(selectedDate, userId) {
        if (userId.isNotBlank()) {
            viewModel.loadData(userId, selectedDate)
        }
    }

    LaunchedEffect(searchText) {
        viewModel.filterList(searchText)
    }

    TransactionsLayout(
        state = viewModel.state.value,
        onDeleteTransaction = { trasaction ->
            viewModel.onDeleteTransaction(trasaction, userId)
        },
        onRefreshData = { viewModel.loadData(userId = userId) },
        onItemClick = navigateToForms
    )
}

@Composable
private fun TransactionsLayout(
    state: TransactionsViewState,
    onDeleteTransaction: (Transacao) -> Unit,
    onRefreshData: () -> Unit,
    onItemClick: (TransactionTypeEnum, Transacao) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF3FB))
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = Color(0xFFFAC853),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 48.dp)
                        .size(60.dp)
                )
            }
            state.isError -> {
                ErrorMessageLayout(onRefreshData)
            }
            state.transactionsAll.isEmpty() -> {
                EmptyListCardLayout("Ops! Você não possui dados registrados este mês.")
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Spacer(modifier = Modifier.size(4.dp))
                    }
                    items(state.transactionsFiltered) { transaction ->
                        SwipeToDismissItem(
                            transaction = transaction,
                            onDeleteItem = onDeleteTransaction,
                            onItemClick = onItemClick
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.size(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: Transacao,
    onClick: (TransactionTypeEnum, Transacao) -> Unit
) {
    val typeEnum = TransactionTypeEnum.getByNome(transaction.tipo ?: "")

    val topColor = if (typeEnum == EXPENSE) {
        colorResource(id = R.color.colorPink)
    } else {
        colorResource(id = R.color.light_green)
    }

    val icon = if (typeEnum == EXPENSE) {
        painterResource(id = R.drawable.ic_despesa_24dp)
    } else {
        painterResource(id = R.drawable.ic_receita_24dp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                if (typeEnum != null) {
                    onClick(typeEnum, transaction)
                }
            },
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
                painter = icon,
                tint = topColor,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f).wrapContentHeight()
            ) {
                Text(
                    text = transaction.nome ?: "Nome",
                    color = topColor,
                    fontSize = 15.sp
                )
                Text(
                    text = transaction.conta ?: "Conta",
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.weight(1f).wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "R$ " + transaction.valor.toMoneyString(),
                    color = topColor,
                    fontSize = 15.sp
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = transaction.data ?: "0000-00-00",
                    color = Color.Gray
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeToDismissItem(
    transaction: Transacao,
    onDeleteItem: (Transacao) -> Unit,
    onItemClick: (TransactionTypeEnum, Transacao) -> Unit
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(EndToStart)) onDeleteItem(transaction)

    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier
            .padding(vertical = Dp(1f)),
        directions = setOf(EndToStart),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == EndToStart) 0.1f else 0.05f)
        },
        background = {
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )
            val color = if (!LocalInspectionMode.current) {
                animateColorAsState(
                    if (dismissState.targetValue == DismissValue.Default) {
                        Color.Gray
                    } else {
                        Color.Red.copy(alpha = 0.7f)
                    }
                ).value
            } else {
                Color.Gray
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEDF3FB))
                    .padding(horizontal = Dp(20f)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    tint = color,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            TransactionItem(transaction = transaction, onClick = onItemClick)
        }
    )
}
