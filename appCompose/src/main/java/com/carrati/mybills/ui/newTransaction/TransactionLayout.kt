package com.carrati.mybills.appCompose.ui.newTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.mybills.appCompose.ui.newTransaction.TransactionTypeEnum.EXPENSE
import com.carrati.mybills.appCompose.ui.newTransaction.TransactionTypeEnum.INCOME
import com.carrati.mybills.appCompose.ui.newTransaction.TransactionTypeEnum.TRANSFER
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Preview
@Composable
fun ExpenseScreenPreview() {
    val state = remember { mutableStateOf(TransactionViewState()) }
    TransactionScreen(EXPENSE, state) { }
}

@Preview
@Composable
fun IncomeScreenPreview() {
    val state = remember { mutableStateOf(TransactionViewState()) }
    TransactionScreen(INCOME, state) { }
}

@Preview
@Composable
fun TransferScreenPreview() {
    val state = remember { mutableStateOf(TransactionViewState()) }
    TransactionScreen(TRANSFER, state) { }
}

@Composable
fun TransactionScreen(
    type: TransactionTypeEnum,
    viewState: MutableState<TransactionViewState>,
    onSaveTransaction: () -> Unit
) {
    Scaffold(
        /*topBar = {
            TopAppBar(
                title = { Text(text = "Despesa", color = Color.Black.copy(alpha = 0.5f)) },
                backgroundColor = Color(0xFFF866A1),
                elevation = 0.dp
            )
        },*/
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSaveTransaction() },
                modifier = Modifier.padding(bottom = 16.dp),
                backgroundColor = Color(0xFFFAC853)
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, top = 8.dp)
            HeaderValue(type, viewState)
            if (type != TRANSFER) FieldPaid(modifier, viewState)
            FieldDate(modifier, viewState)
            if (type != TRANSFER) FieldDescription(modifier, viewState, onSaveTransaction)
            FieldAccount(modifier, viewState, isAccount1 = true)
            if (type == TRANSFER) FieldAccount(modifier, viewState, isAccount1 = false)
        }
    }
}

@Composable
fun HeaderValue(
    type: TransactionTypeEnum,
    viewState: MutableState<TransactionViewState>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                when (type) {
                    EXPENSE -> Color(0xFFF866A1)
                    INCOME -> Color(0xFF2AA653)
                    TRANSFER -> Color(0xFF33B5E5)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.padding(start = 32.dp),
            text = "R\$ ",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black.copy(alpha = 0.5f)
        )

        TextField(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.End),
            value = "R$%.2f".format(viewState.value.amount),
            onValueChange = {
                viewState.value.amount = it.toFloatOrNull() ?: 0f
            },
            textStyle = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.Black.copy(alpha = 0.6f),
                backgroundColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun FieldPaid(
    modifier: Modifier,
    viewState: MutableState<TransactionViewState>
) {
    TextField(
        value = "",
        onValueChange = { },
        modifier = modifier.padding(top = 16.dp),
        label = { Text(text = "Pago") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.DarkGray.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            Switch(
                checked = viewState.value.paid,
                onCheckedChange = { viewState.value.paid = !viewState.value.paid },
                modifier = Modifier.padding(end = 8.dp)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Black.copy(alpha = 0.6f)
        ),
        readOnly = true,
        singleLine = true
    )
}

@Composable
fun FieldDate(
    modifier: Modifier,
    viewState: MutableState<TransactionViewState>
) {
    TextField(
        value = viewState.value.date,
        onValueChange = { viewState.value.date = it },
        modifier = modifier,
        label = { Text(text = "Data") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                tint = Color.DarkGray.copy(alpha = 0.7f)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Black.copy(alpha = 0.6f)
        ),
        readOnly = true,
        singleLine = true
    )
}

@Composable
fun FieldDescription(
    modifier: Modifier,
    viewState: MutableState<TransactionViewState>,
    onSaveTransaction: () -> Unit
) {
    TextField(
        value = viewState.value.description,
        onValueChange = { viewState.value.description = it },
        modifier = modifier,
        label = { Text(text = "Descrição") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = Color.DarkGray.copy(alpha = 0.7f)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onSaveTransaction() }),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Black.copy(alpha = 0.6f)
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FieldAccount(
    modifier: Modifier,
    viewState: MutableState<TransactionViewState>,
    isAccount1: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = if (isAccount1) viewState.value.account1 else viewState.value.account2,
            onValueChange = {
                if (isAccount1) {
                    viewState.value.account1 = it
                } else {
                    viewState.value.account2 = it
                }
            },
            label = { Text("Conta") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = Color.DarkGray.copy(alpha = 0.7f)
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.Black.copy(alpha = 0.6f)
            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            viewState.value.listAccounts.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        if (isAccount1) {
                            viewState.value.account1 = selectionOption
                        } else {
                            viewState.value.account2 = selectionOption
                        }
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}
