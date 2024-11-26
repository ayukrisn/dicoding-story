package com.ayukrisna.dicodingstory.view.ui.screen.liststory

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.view.ui.component.AppBar
import com.ayukrisna.dicodingstory.view.ui.screen.login.LoginViewModel
import com.ayukrisna.dicodingstory.view.ui.theme.DicodingStoryTheme
import org.koin.androidx.compose.koinViewModel
import com.ayukrisna.dicodingstory.util.Result

@Composable
fun ListStoryScreen (
    viewModel: ListStoryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val storiesState by viewModel.storiesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStories()
    }


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
                    when (storiesState) {
                        is Result.Idle -> Text("Idle State")
                        is Result.Loading -> CircularProgressIndicator()
                        is Result.Success -> {
                            val stories: List<ListStoryItem> = (storiesState as Result.Success<List<ListStoryItem>>).data
                            if (stories.isNotEmpty()) {
                                LazyColumn {
                                    items(stories) { story ->
                                        ItemListStory(
                                            photoUrl = story.photoUrl ?: "https://picsum.photos/seed/picsum/200/300",
                                            name = story.name ?: "No Name",
                                            description = story.description ?: "No Description"
                                        )
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListStoryAppBar(title: String, subtitle: String) {
    AppBar(title, subtitle)
}

@Composable
fun ItemListStory(
    name: String,
    description: String,
    photoUrl: String,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceBright
                ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
//            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListStoryItem(modifier : Modifier = Modifier) {
    ItemListStory(
        photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1732633121592_9a49bbb02b35aad5f8a1.jpg",
        name = "Sample Story",
        description = "This is a description of the story. It is concise and informative."
    )
}
