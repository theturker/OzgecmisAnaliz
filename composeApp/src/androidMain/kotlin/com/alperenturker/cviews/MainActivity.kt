package com.alperenturker.cviews

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        window.statusBarColor = Color.parseColor("#FAFAFA")
        window.navigationBarColor = Color.parseColor("#FAFAFA")

        setContent {
            CvRehberiApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(selectedPdf = null, onFilePicked = { _, _ -> }, onRequestPickFile = {})
}