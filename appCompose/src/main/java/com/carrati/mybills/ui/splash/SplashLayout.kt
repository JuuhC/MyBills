package com.carrati.mybills.appCompose.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carrati.mybills.appCompose.R.drawable

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF33B5E5)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 64.dp),
            painter = painterResource(id = drawable.ic_female_wallet),
            contentDescription = "App logo"
        )
    }
}
