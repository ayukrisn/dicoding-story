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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.ayukrisna.dicodingstory.R
import com.ayukrisna.dicodingstory.view.ui.component.CenterAppBar
import com.ayukrisna.dicodingstory.view.ui.component.LargeTextArea
import java.io.File

@Composable
fun AddStoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val directory = context.getExternalFilesDir("pictures")
    var selectedUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val onSetUri: (Uri) -> Unit = { newUri -> selectedUri = newUri }
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
                title = "Add Story",
                onBackClick = { onBackClick() }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
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
                    Text(text = "No image selected.")
                }

                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add Photo")
                }
                Spacer(modifier = Modifier.height(16.dp))
                AddStoryField()
            }
        }
    )
}

@Composable
fun AddStoryAppBar(title: String, onBackClick: () -> Unit) {
    CenterAppBar(title = title, onBackClick = onBackClick)
}

@Composable
fun AddStoryField() {
    var text by remember { mutableStateOf("") }

    LargeTextArea(
        text = text,
        onValueChange = { newText -> text = newText },
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
