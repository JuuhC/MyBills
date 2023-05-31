package com.carrati.mybills.appCompose.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carrati.mybills.appCompose.R.drawable

@Preview
@Composable
private fun Preview() {
    EmptyListLayout()
}

@Composable
fun EmptyListLayout() {
    Card(
        modifier = Modifier.padding(24.dp),
        shape = RoundedCornerShape(64.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = drawable.img_no_data),
                contentDescription = "Icon refresh",
                modifier = Modifier.size(300.dp).align(Companion.TopCenter)
            )
            Text(
                modifier = Modifier
                    .align(Companion.TopCenter)
                    .padding(top = 250.dp, start = 24.dp, end = 24.dp),
                text = "Ops! Você não possui dados registrados este mês.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
            Text(
                modifier = Modifier
                    .padding(top = 308.dp, bottom = 64.dp, start = 48.dp, end = 48.dp)
                    .align(Companion.TopCenter),
                text = "Para criar um novo item, clique no botão (+)",
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
