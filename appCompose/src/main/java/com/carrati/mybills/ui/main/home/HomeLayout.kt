package com.carrati.mybills.appCompose.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.domain.models.Conta
import com.carrati.mybills.appCompose.ui.main.home.HomeViewModel
import com.carrati.mybills.appCompose.ui.main.home.HomeViewState
import java.util.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Preview
@Composable
fun HomeScreenPreview() {
    val state by remember { mutableStateOf(HomeViewState()) }
    state.contas = listOf(
        Conta("Conta", 10.0),
        Conta("Conta", 20.0),
        Conta("Conta", 30.0)
    )
    HomeLayout(state) { _, _ -> }
}

@Composable
fun HomeScreen(selectedDate: MutableState<Calendar>, userId: String) {
    val viewModel: HomeViewModel = koinViewModel { parametersOf(userId) }
    viewModel.loadData(selectedDate.value)
    HomeLayout(state = viewModel.state.value) { accountName, initialValue ->
        viewModel.onAddConta(accountName, initialValue)
    }
}

@Composable
private fun HomeLayout(state: HomeViewState, onCreateAccount: (String, Double) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF3FB))
    ) {
        val showCreateAccountDialog = remember { mutableStateOf(false) }

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
                            color = Color(0xFF8F8181)
                        )
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { showCreateAccountDialog.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color(0xF38F8181),
                                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                            )
                        }
                    }
                }
                item {
                    CardConta(state.contas)
                }
            }

            AnimatedVisibility(visible = showCreateAccountDialog.value) {
                CreateAccountDialog(
                    onConfirmButton = onCreateAccount,
                    setVisible = { showCreateAccountDialog.value = it }
                )
            }
        }
    }
}

@Composable
private fun SaldoCard(
    saldoTotal: Double,
    receitas: Double,
    despesas: Double,
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
                        color = Color(0xFF2AA653),
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
                        color = Color(0xFFF866A1),
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
private fun CardConta(
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
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {
                            })
                        },
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

@Composable
private fun CreateAccountDialog(
    onConfirmButton: (String, Double) -> Unit,
    setVisible: (Boolean) -> Unit
) {
    var accountName by remember { mutableStateOf("teste") }
    var initialValue by remember { mutableStateOf(0.0) }

    AlertDialog(
        onDismissRequest = { setVisible(false) },
        confirmButton = {
            TextButton(onClick = {
                onConfirmButton(accountName, initialValue)
                setVisible(false)
            }) {
                Text("OK", color = Color(0xFF33B5E5))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                setVisible(false)
            }) {
                Text("CANCELAR", color = Color(0xFF33B5E5))
            }
        },
        title = { Text(text = "Adicionar Conta", fontSize = 20.sp) },
        text = {
            Column {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Preencha os dados da conta:"
                )
                OutlinedTextField(
                    modifier = Modifier.padding(vertical = 8.dp),
                    value = accountName,
                    label = { Text(text = "Nome da conta") },
                    onValueChange = { accountName = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.padding(top = 8.dp),
                    value = "R$%.2f".format(initialValue),
                    label = { Text(text = "Saldo inicial") },
                    onValueChange = { initialValue = it.toDoubleOrNull() ?: 0.0 },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }
    )
}
