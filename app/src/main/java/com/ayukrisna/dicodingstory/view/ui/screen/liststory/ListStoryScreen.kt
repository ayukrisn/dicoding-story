package com.ayukrisna.dicodingstory.view.ui.screen.liststory

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import com.ayukrisna.dicodingstory.util.Result
import com.ayukrisna.dicodingstory.view.ui.component.AppBar
import com.ayukrisna.dicodingstory.view.ui.component.LoadingProgress
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListStoryScreen (
    onClick: (String) -> Unit,
    onNavigateToAddStory: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: ListStoryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val storiesState by viewModel.storiesState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchStories()
    }

    Scaffold(
        topBar = {
            ListStoryAppBar(
                title = stringResource(R.string.dicoding_story),
                subtitle = stringResource(R.string.dicoding_subtitle),
                actionIcon = Icons.AutoMirrored.Filled.ExitToApp,
                actionIcon2 = Icons.Filled.Menu,
                onActionClick = {
                    showDialog.value = true
                },
                onActionClick2 = {
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    context.startActivity(intent)
                }
            )
        },
        floatingActionButton = {
            AddStoryButton(onClick = onNavigateToAddStory)
        },
        content = { paddingValues ->
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                ) {
                    when (storiesState) {
                        is Result.Idle -> Text("Idle State")
                        is Result.Loading -> LoadingProgress()
                        is Result.Success -> {
                            val stories: List<ListStoryItem> = (storiesState as Result.Success<List<ListStoryItem>>).data
                            if (stories.isNotEmpty()) {
                                LazyColumn {
                                    items(stories) { story ->
                                        ItemListStory(
                                            photoUrl = story.photoUrl ?: "https://picsum.photos/seed/picsum/200/300",
                                            name = story.name ?: "No Name",
                                            description = story.description ?: "No Description",
                                            onClick = {
                                                story.id?.let { onClick(it) }
                                            }
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
            LogoutAlertDialog(
                showDialog = showDialog,
                onConfirm = {
                    viewModel.logOut()
                    onLogOut()
                },
                onDismiss = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListStoryAppBar(
    title: String,
    subtitle: String,
    actionIcon: ImageVector,
    actionIcon2: ImageVector,
    onActionClick: () -> Unit,
    onActionClick2: () -> Unit) {
    AppBar(
        title,
        subtitle,
        actionIcon,
        actionIcon2,
        onActionClick,
        onActionClick2
    )
}

@Composable
fun ItemListStory(
    name: String,
    description: String,
    photoUrl: String,
    onClick: () -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
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
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun AddStoryButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Edit, "Add story action button.") },
        text = { Text(text = stringResource(R.string.add_story)) },
    )
}

@Composable
fun LogoutAlertDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(text = stringResource(R.string.logout), style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Text(text = stringResource(R.string.logout_confirmation))
            },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    onConfirm()
                }) {
                    Text(text = stringResource(R.string.logout))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog.value = false
                    onDismiss()
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}