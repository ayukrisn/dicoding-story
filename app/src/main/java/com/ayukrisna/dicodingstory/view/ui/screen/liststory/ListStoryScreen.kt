package com.ayukrisna.dicodingstory.view.ui.screen.liststory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.view.ui.component.AppBar
import com.ayukrisna.dicodingstory.view.ui.component.CenterAppBar
import com.ayukrisna.dicodingstory.view.ui.theme.DicodingStoryTheme

@Composable
fun ListStoryScreen (modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            ListStoryAppBar("Dicoding Story", "Ceritakan kisahmu")
        },
        content = { paddingValues ->
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                ) {

                }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewListStoryScene() {
    DicodingStoryTheme {
        ListStoryScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListStoryAppBar(title: String, subtitle: String) {
    AppBar(title, subtitle)
}