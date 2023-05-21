package com.carrati.mybills.appCompose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.domain.models.Conta
import com.carrati.mybills.ui.main.home.HomeViewState

@Preview
@Composable
private fun Preview() {
    val state by remember { mutableStateOf(HomeViewState()) }
    state.contas = listOf(Conta("Conta", 10.0), Conta("Conta", 20.0), Conta("Conta", 30.0))
    HomeScreen(state) {}
}

@Composable
fun HomeScreen(state: HomeViewState, onAddConta: () -> Unit) {
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    SaldoCard(
                        saldoTotal = state.saldo,
                        receitas = state.receitas,
                        despesas = state.despesas,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp),
                            color = Color(0xFF776D6D)
                        )
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { onAddConta() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color(0xFF33B5E5),
                                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                            )
                        }
                    }
                }
                item {
                    CardConta(state.contas)
                }
            }
        }
    }
}

@Composable
fun SaldoCard(
    saldoTotal: Float,
    receitas: Float,
    despesas: Float,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        elevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Text(
                text = "Saldo Total",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "R$%.2f".format(saldoTotal),
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Receitas",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "R$%.2f".format(receitas),
                        color = Color(0xFF118336),
                        fontSize = 17.sp
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Despesas",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                    Text(
                        text = "R$%.2f".format(despesas),
                        color = Color(0xFFED4588),
                        fontSize = 17.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun CardConta(
    contas: List<Conta>
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            contas.forEach { conta ->
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = conta.nome.orEmpty(),
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "R$%.2f".format(conta.saldo),
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
