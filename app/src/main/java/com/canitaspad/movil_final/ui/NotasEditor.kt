package com.canitaspad.movil_final.ui

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.canitaspad.movil_final.AppViewModelProvider
import com.canitaspad.movil_final.ComposeFileProvider
import com.canitaspad.movil_final.R
import com.canitaspad.movil_final.VideoPlayer
import com.canitaspad.movil_final.viewModel.NotaDetails
import com.canitaspad.movil_final.viewModel.NotaUiState
import com.canitaspad.movil_final.viewModel.NotasEditorViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var audioUri: Uri? = null

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditorNotas(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NotasEditorViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    onClickStRe: () -> Unit,
    onClickSpRe: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var videoUris by remember { mutableStateOf(listOf<Uri>()) }
    var audioUris by remember { mutableStateOf(listOf<Uri>()) }

    val context = LocalContext.current
    val audioRecorder = AndroidAudioRecorder(context)

    // Multimedia
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUris = imageUris + it
                viewModel.imageUris += it
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUris = imageUris + (imageUri ?: return@rememberLauncherForActivityResult)
                viewModel.imageUris += (imageUri ?: return@rememberLauncherForActivityResult)
            }
        }
    )

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { success ->
            if (success) {
                videoUris = videoUris + (videoUri ?: return@rememberLauncherForActivityResult)
                viewModel.videoUris += (videoUri ?: return@rememberLauncherForActivityResult)
            }
        }
    )

    // Audio Permission State
    val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    // Dialog for rationale state
    var rationaleState by remember { mutableStateOf<RationaleState?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { navController.navigate(Routes.NotasScreen.route) }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar", modifier = Modifier.size(32.dp))
            }
        }

        NotaEntryBody(
            notaUiState = viewModel.notaUiState,
            onNotaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateUiState(viewModel.notaUiState.notaDetails)
                    viewModel.saveNota()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(5.dp).verticalScroll(rememberScrollState()).fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { cameraLauncher.launch(ComposeFileProvider.getImageUri(context)) }, modifier = Modifier.weight(1f)) {
                Image(painter = painterResource(R.drawable.camara_fotografica), modifier = Modifier.size(35.dp))
            }
            Button(onClick = { videoLauncher.launch(ComposeFileProvider.getVideoUri(context)) }, modifier = Modifier.weight(1f)) {
                Image(painter = painterResource(R.drawable.video), modifier = Modifier.size(35.dp))
            }
            Button(onClick = { imagePicker.launch("image/*") }, modifier = Modifier.weight(1f)) {
                Image(painter = painterResource(R.drawable.carpeta), modifier = Modifier.size(35.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PermissionRequestButton(
            isGranted = recordAudioPermissionState.status.isGranted,
            title = stringResource(R.string.record_audio),
            onClickStGra = onClickStGra,
            onClickSpGra = onClickSpGra,
            audioUris = viewModel.audioUris,
            onClick = {
                if (recordAudioPermissionState.status.shouldShowRationale) {
                    rationaleState = RationaleState("Permiso para grabar audio", "In order to use this feature please grant access by accepting the grabar audio dialog.") {
                        if (it) recordAudioPermissionState.launchPermissionRequest()
                        rationaleState = null
                    }
                } else {
                    recordAudioPermissionState.launchPermissionRequest()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            LazyColumn(modifier = Modifier.align(Alignment.Center)) {
                items(imageUris + videoUris + viewModel.audioUris) { uri ->
                    MultimediaCard(uri, imageUris, videoUris, viewModel)
                }
            }
        }
    }
}

@Composable
fun MultimediaCard(uri: Uri, imageUris: List<Uri>, videoUris: List<Uri>, viewModel: NotasEditorViewModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column {
            when {
                uri in imageUris -> AsyncImage(model = uri, modifier = Modifier.height(400.dp).fillMaxWidth(), contentDescription = "Selected image")
                uri in videoUris -> VideoPlayer(videoUri = uri, modifier = Modifier.height(400.dp).fillMaxWidth())
                uri in viewModel.audioUris -> AudioPlayerCard(uri, viewModel)
            }

            Button(onClick = {
                // Elimina la tarjeta y quita la imagen del arreglo.
                imageUris = imageUris.filter { it != uri }
                videoUris = videoUris.filter { it != uri }
                viewModel.removeUri(uri)
            }, modifier = Modifier.align(Alignment.End)) {
                Image(painter = painterResource(R.drawable.eliminar), modifier = Modifier.size(25.dp))
            }
        }
    }
}

@Composable
fun AudioPlayerCard(uri: Uri, viewModel: NotasEditorViewModel) {
    val context = LocalContext.current
    val audioPlayer = AndroidAudioPlayer2(context, uri)

    PermissionRequestButton2(
        isGranted = recordAudioPermissionState.status.isGranted,
        title = stringResource(R.string.record_audio),
        onClickStGra = {},
        onClickSpGra = {},
        onClickStRe = { audioPlayer.start(uri) },
        onClickSpRe = { audioPlayer.stop() },
        audioUris = viewModel.audioUris,
        onClick = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PermissionRequestButton(
    isGranted: Boolean, title: String,
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    audioUris: List<Uri>,
    onClick: () -> Unit,
) {
    if (isGranted) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onClickStGra, modifier = Modifier.weight(1f)) {
                Image(painter = painterResource(R.drawable.microfono_grabador), modifier = Modifier.size(25.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                onClickSpGra()
                audioUris.plus(audioUri!!)
            }, modifier = Modifier.weight(1f)) {
                Image(painter = painterResource(R.drawable.cuadra), modifier = Modifier.size(25.dp))
            }
        }
    } else {
        Button(onClick = onClick) {
            Text("Request $title")
        }
    }
}
