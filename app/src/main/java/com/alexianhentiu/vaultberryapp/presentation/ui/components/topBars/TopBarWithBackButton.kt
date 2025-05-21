package com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons.DebouncedBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackButton(
    navController: NavController,
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            DebouncedBackButton(navController)
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun TopBarWithBackButtonPreview() {
    TopBarWithBackButton(
        navController = NavController(LocalContext.current),
        title = "Title"
    )
}