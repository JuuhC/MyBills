package com.carrati.mybills.appCompose.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    SplashScreen()
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF33B5E5)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.padding(bottom = 150.dp).size(240.dp),
            painter = painterResource(id = drawable.ic_female_wallet),
            contentDescription = "App logo"
        )
        Text(
            modifier = Modifier.padding(top = 70.dp),
            text = "MyBills",
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Cursive,
            textAlign = TextAlign.Center
        )
        CircularProgressIndicator(
            color = Color(0xFFFAC853),
            modifier = Modifier
                .padding(top = 550.dp)
                .size(50.dp)
        )
    }
}
