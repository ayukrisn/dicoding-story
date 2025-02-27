package com.ayukrisna.dicodingstory.view.ui.screen.auth.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToWelcome: () -> Unit,
    onNavigateToListStory: () -> Unit,
){
    val user = viewModel.userSession.collectAsState()
    var isSplashDone by remember { mutableStateOf(false) }

    Surface (modifier = Modifier.fillMaxSize() ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_dicoding),
                contentDescription = "Splash Image",
                modifier = Modifier
                    .size(250.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // Ensure the splash screen always waits for 3 seconds
        isSplashDone = true
    }

    if (isSplashDone) {
        if (user.value.isLogin) {
            onNavigateToListStory()
        } else {
            onNavigateToWelcome()
        }
    }
}