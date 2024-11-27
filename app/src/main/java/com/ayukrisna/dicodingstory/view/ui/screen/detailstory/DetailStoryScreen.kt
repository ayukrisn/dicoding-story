package com.ayukrisna.dicodingstory.view.ui.screen.detailstory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.data.remote.response.Story
import com.ayukrisna.dicodingstory.util.Result
import com.ayukrisna.dicodingstory.view.ui.component.CenterAppBar
import com.ayukrisna.dicodingstory.view.ui.component.LoadingProgress
import com.ayukrisna.dicodingstory.view.ui.component.formatTimestamp
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailStoryScreen (
    id: String,
    onBackClick: () -> Unit,
    viewModel: DetailStoryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val storyState by viewModel.storyState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStory(id)
    }

    Scaffold (
        topBar = {
            DetailStoryAppBar(
                title = "Story Details",
                onBackClick = onBackClick
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
            ) {
                when (storyState) {
                    is Result.Idle -> Text("Idle State")
                    is Result.Loading -> LoadingProgress()
                    is Result.Success -> {
                        val story: Story = (storyState as Result.Success<Story>).data
                        DetailStory(
                            photoUrl = story.photoUrl ?: "https://picsum.photos/seed/picsum/200/300",
                            name = story.name ?: "No Name",
                            description = story.description ?: "No Description",
                            time = ("Created at: " + story.createdAt?.let { formatTimestamp(it) })
                        )
                    }
                    is Result.Error -> {
                        val error = (storyState as Result.Error).error
                        Text("Error: $error")
                    }
                }
            }
        }
    )
}

@Composable
fun DetailStoryAppBar(title: String, onBackClick: () -> Unit) {
    CenterAppBar(title, onBackClick)
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailStory() {
    DetailStory(
        photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1732633121592_9a49bbb02b35aad5f8a1.jpg",
        name = "Sample Story",
        description = "This is a description of the story. It is concise and informative.",
        time = "22 Jan"
    )
}

@Composable
fun DetailStory(
    name: String,
    description: String,
    photoUrl: String,
    time: String,
    modifier : Modifier = Modifier
) {
    Column {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Placeholder Image",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "Calendar Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}