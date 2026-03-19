package com.alperenturker.cviews.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

enum class BottomTab {
    Upload,
    Analysis,
    History,
}

@Composable
fun BottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
    ) {
        NavigationBarItem(
            selected = selectedTab == BottomTab.Upload,
            onClick = { onTabSelected(BottomTab.Upload) },
            icon = { Icon(imageVector = Icons.Filled.Upload, contentDescription = null) },
            label = { Text("CV Yükle") },
        )
        NavigationBarItem(
            selected = selectedTab == BottomTab.Analysis,
            onClick = { onTabSelected(BottomTab.Analysis) },
            icon = { Icon(imageVector = Icons.Filled.Assessment, contentDescription = null) },
            label = { Text("Analiz") },
        )
        NavigationBarItem(
            selected = selectedTab == BottomTab.History,
            onClick = { onTabSelected(BottomTab.History) },
            icon = { Icon(imageVector = Icons.Filled.Description, contentDescription = null) },
            label = { Text("Geçmiş") },
        )
    }
}

