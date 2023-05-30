package com.carrati.mybills.appCompose.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.carrati.mybills.appCompose.R

@Preview
@Composable
fun LoginScreenPreview() {
    val isLoading = remember { mutableStateOf(false) }
    LoginScreen(isLoading) {}
}

@Composable
fun LoginScreen(isLoading: MutableState<Boolean>, onSignInWithGoogle: () -> Unit) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it).fillMaxSize().background(Color(0xFF33B5E5)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 48.dp),
                text = "MyBills",
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.padding(horizontal = 64.dp),
                painter = painterResource(id = R.drawable.ic_piggy_bank),
                contentDescription = "Piggy bank"
            )
            Text(
                modifier = Modifier.padding(bottom = 48.dp).padding(horizontal = 48.dp),
                text = "Mapeie seus gastos e comece a economizar!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 48.dp, bottom = 16.dp),
                text = "Começar agora:",
                color = Color.White
            )
            SignInWithGoogleButton(onSignInWithGoogle)
        }
    }
}

@Composable
fun SignInWithGoogleButton(onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.DarkGray
        )
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_logo_google),
            contentDescription = ""
        )
        Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
    }
}