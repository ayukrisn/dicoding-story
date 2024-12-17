package com.ayukrisna.dicodingstory.view.ui.screen.maps


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.util.Result
import com.ayukrisna.dicodingstory.view.ui.component.LoadingProgress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    viewModel: MapsViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val defaultLocation = LatLng(-6.2, 106.81)
    val storiesState by viewModel.storiesState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchStories()
    }

    LaunchedEffect(storiesState) {
        if (storiesState is Result.Success) {
            val stories = (storiesState as Result.Success<List<ListStoryItem>>).data
            if (stories.isNotEmpty()) {
                val firstStory = stories.first()
                val firstStoryLocation = LatLng(firstStory.lat!!, firstStory.lon!!)
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(firstStoryLocation, 1f))
            }
        }
    }

    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }

    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }

        when (storiesState) {
            is Result.Idle -> Text("Idle State", modifier = Modifier.fillMaxSize())
            is Result.Loading -> LoadingProgress()
            is Result.Success -> {
                val stories: List<ListStoryItem> = (storiesState as Result.Success<List<ListStoryItem>>).data
                if (stories.isNotEmpty()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = properties,
                        uiSettings = uiSettings
                    ) {
                        stories.forEach { story ->
                            MarkerInfoWindow(
                                state = MarkerState(position = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)),
                            ) {
                                Column (
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10))
                                        .background(MaterialTheme.colorScheme.surfaceBright)
                                        .padding(20.dp)
                                ) {
                                    Text(story.name ?: "No Name", fontWeight = FontWeight.Bold)
                                    Text(story.description ?: "No Description", fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
            } else {
                    Text("No stories available")
                }
            }
            is Result.Error -> {
                val error = (storiesState as Result.Error).error
                Text("Error: $error")
            }
        }
    }