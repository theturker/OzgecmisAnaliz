package com.alperenturker.cviews

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

@Composable
fun CvRehberiApp() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        initPdfBox(context)
    }
    var selectedPdf by remember { mutableStateOf<Pair<ByteArray?, String?>?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        context.contentResolver.openInputStream(uri)?.use { stream: InputStream ->
            val bytes = stream.readBytes()
            val name = uri.lastPathSegment ?: "document.pdf"
            selectedPdf = Pair(bytes, name)
        }
    }
    App(
        selectedPdf = selectedPdf,
        onFilePicked = { bytes, name -> selectedPdf = Pair(bytes, name) },
        onRequestPickFile = { launcher.launch("application/pdf") },
    )
}
