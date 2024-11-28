package com.ayukrisna.dicodingstory.view.ui.screen.addstory

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.util.Result
import com.ayukrisna.dicodingstory.view.ui.component.CenterAppBar
import com.ayukrisna.dicodingstory.view.ui.component.LargeTextArea
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * Note to self: this part needs tons of work and clean up....
 */
@Composable
fun AddStoryScreen(
    viewModel: AddStoryViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val addStoryState by viewModel.addStoryState.observeAsState(initial = Result.Loading)

    val directory = context.getExternalFilesDir("pictures")
    var selectedUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val onSetUri: (Uri) -> Unit = { newUri ->
        selectedUri = newUri
        viewModel.onEvent(AddStoryEvent.UriChanged(newUri))
    }
    val tempUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    // Get Temporary URI
    fun getTempUri(): Uri? {
        return try {
            if (directory == null) {
                Toast.makeText(context, "Directory is not initialized.", Toast.LENGTH_SHORT).show()
                return null
            }

            if (!directory.exists() && !directory.mkdirs()) {
                Toast.makeText(context, "Failed to create directory.", Toast.LENGTH_SHORT).show()
                return null
            }

            val file = File.createTempFile("image_${System.currentTimeMillis()}", ".jpg", directory)
            FileProvider.getUriForFile(context, authority, file)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to create temp file for photo.", Toast.LENGTH_SHORT).show()
            null
        }
    }


    // Get image picker
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { selectedUri ->
                onSetUri(selectedUri)
            }
        }
    )

    // Take photo launcher
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            if (isSaved) {
                tempUri.value?.let { onSetUri(it) }
            }
        }
    )

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            tempUri.value?.let { takePhotoLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission is required to take photos.", Toast.LENGTH_LONG).show()
        }
    }

    // Show bottom sheet
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        MyModalBottomSheet(
            onDismiss = {
                showBottomSheet = false
            },
            onTakePhotoClick = {
                showBottomSheet = false

                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    val tmpUri = getTempUri()
                    tempUri.value = tmpUri
                    tempUri.value?.let { takePhotoLauncher.launch(it) }
                } else {
                    cameraPermissionLauncher.launch(permission)
                }
            },
            onPhotoGalleryClick = {
                showBottomSheet = false
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        )
    }

    Scaffold(
        topBar = {
            AddStoryAppBar(
                title = stringResource(R.string.add_story),
                onBackClick = { onBackClick() }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedUri != null) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = selectedUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Text(text = stringResource(R.string.no_image_selected))
                }

                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.add_photo))
                }
                Spacer(modifier = Modifier.height(16.dp))
                AddStoryField(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                AddStoryButton(viewModel)
                Spacer(modifier = Modifier.height(4.dp))
                when (addStoryState) {
                    is Result.Idle -> {}
                    is Result.Loading -> Text("Uploading...")
                    is Result.Success<*> -> {
                        var showDialog by remember { mutableStateOf(true) }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text(text = "Success") },
                                text = { Text(
                                    stringResource(
                                        R.string.story_upload_success
                                    )) },
                                confirmButton = {
                                    Button(onClick = {
                                        showDialog = false
                                        onBackClick()
                                    }) {
                                        Text("OK")
                                    }
                                }
                            )
                        }
                    }
                    is Result.Error -> {
                        val error = (addStoryState as Result.Error).error
                        Text("Error: $error", color = Color.Red)
                    }
                }
            }
        }
    )
}

@Composable
fun AddStoryAppBar(title: String, onBackClick: () -> Unit) {
    CenterAppBar(title = title, onBackClick = onBackClick)
}

@Composable
fun AddStoryField(viewModel: AddStoryViewModel) {
    LargeTextArea(
        text = viewModel.draftState.storyDraft,
        onValueChange = {
            viewModel.onEvent(AddStoryEvent.StoryChanged(it))
        },
        isError = viewModel.draftState.storyError != null,
        errorMessage = viewModel.draftState.storyError,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Choose an Option", modifier = Modifier.padding(bottom = 4.dp))

            Button(
                onClick = onTakePhotoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(text = "Take Photo")
            }

            Button(
                onClick = onPhotoGalleryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(text = "Pick from Gallery")
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun AddStoryButton(
    viewModel: AddStoryViewModel,
    modifier: Modifier = Modifier
){
    Button(onClick = {
        viewModel.onEvent(AddStoryEvent.Submit)
    },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 8.dp)) {
        Text(text = stringResource(R.string.add_story))
    }
}
