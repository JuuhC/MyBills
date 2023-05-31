package com.carrati.mybills.appCompose.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.mybills.appCompose.R.drawable

@Preview
@Composable
private fun Preview() {
    ErrorMessageLayout {}
}

@Composable
fun ErrorMessageLayout(onRefreshData: () -> Unit) {
    Card(
        modifier = Modifier.padding(24.dp),
        shape = RoundedCornerShape(64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = drawable.img_server_error),
                contentDescription = "Icon refresh",
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().height(250.dp)
            )
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Erro ao carregar dados.\nAperte para tentar novamente.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 24.dp, top = 16.dp).size(38.dp),
                backgroundColor = Color(0xFFFAC853),
                elevation = FloatingActionButtonDefaults.elevation(1.dp),
                onClick = { onRefreshData() }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_refresh_24dp),
                    contentDescription = "Icon refresh",
                    tint = Color.White,
                    modifier = Modifier.padding(7.dp)
                )
            }
        }
    }
}
