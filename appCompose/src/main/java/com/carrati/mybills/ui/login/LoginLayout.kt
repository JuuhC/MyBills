package com.carrati.mybills.appCompose.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion
import com.carrati.mybills.appCompose.R

@Preview
@Composable
private fun Preview() {
    val isLoading by remember { mutableStateOf(false) }
    LoginScreen(isLoading) {}
}

@Composable
fun LoginScreen(isLoading: Boolean, onSignInWithGoogle: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().background(Color(0xFF33B5E5))
    ) {
        val (text1, image, text2, text3, button) = createRefs()
        val guideline = createGuidelineFromTop(0.65f)

        Text(
            modifier = Modifier.constrainAs(text1) {
                top.linkTo(parent.top, 64.dp)
                bottom.linkTo(image.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = "MyBills",
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Cursive,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier.constrainAs(image) {
                top.linkTo(text1.bottom, margin = 16.dp)
                bottom.linkTo(guideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Companion.fillToConstraints
            },
            painter = painterResource(id = R.drawable.img_personal_finance),
            contentDescription = "Piggy bank"
        )
        Text(
            modifier = Modifier.constrainAs(text2) {
                top.linkTo(image.bottom)
                start.linkTo(parent.start, margin = 48.dp)
                end.linkTo(parent.end, margin = 48.dp)
                width = Dimension.fillToConstraints
            },
            text = "Mapeie seus gastos e comece a economizar!",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.constrainAs(text3) {
                bottom.linkTo(button.top, 12.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = "Começar agora:",
            color = Color.White,
            fontFamily = FontFamily.SansSerif
        )
        SignInWithGoogleButton(
            isLoading,
            onSignInWithGoogle,
            Modifier.constrainAs(button) {
                top.linkTo(text2.bottom, margin = 48.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun SignInWithGoogleButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.DarkGray
        )
    ) {
        if (isLoading.not()) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_logo_google),
                contentDescription = ""
            )
            Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
        } else {
            LinearProgressIndicator(
                color = Color(0xFFFAC853),
                modifier = Modifier
                    .padding(vertical = 13.dp, horizontal = 24.dp)
            )
        }
    }
}
