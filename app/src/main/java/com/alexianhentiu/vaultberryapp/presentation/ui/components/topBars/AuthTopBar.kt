package com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(
    modifier: Modifier = Modifier,
    titleText: String = stringResource(R.string.app_name),
    onSettingsClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                modifier = modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings_icon_content_description)
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun AuthTopBarPreview() {
    AuthTopBar()
}