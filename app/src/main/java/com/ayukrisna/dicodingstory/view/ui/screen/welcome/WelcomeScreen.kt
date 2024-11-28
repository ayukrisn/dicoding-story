package com.ayukrisna.dicodingstory.view.ui.screen.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.view.ui.component.AnimatedMovingImageVertical


@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 42.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            AnimatedWelcomeImage()
            Text(
                stringResource(R.string.dicoding_story),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 8.dp))
            Text(
                stringResource(R.string.dicoding_introduction),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp, 0.dp, 8.dp))
            //Log In Button
            LoginButton({ onNavigateToLogin() })
            //Signup Button
            SignupButton({ onNavigateToSignup() })
        }
    }
}

@Composable
fun LoginButton(onNavigateToLogin: () -> Unit, modifier: Modifier = Modifier){
    Button(onClick = { onNavigateToLogin() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 4.dp)) {
        Text("Log In")
    }
}

@Composable
fun SignupButton(onNavigateToSignup: () -> Unit, modifier: Modifier = Modifier){
    OutlinedButton(onClick = { onNavigateToSignup() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 8.dp)) {
        Text("Sign Up")
    }
}

@Composable
fun AnimatedWelcomeImage() {
    AnimatedMovingImageVertical(
        painter = painterResource(id = R.drawable.image_signup),
        description = "Welcome Image Animation"
    )
}