package com.alexianhentiu.vaultberryapp.presentation.ui.components.bars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackButton(
    onBackClick: () -> Unit,
    title: String
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun TopBarWithBackButtonPreview() {
    TopBarWithBackButton(
        onBackClick = {},
        title = "Title"
    )
}