package com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    titleText: String = stringResource(R.string.app_name)
) {
    TopAppBar(
        title = {
            Text(
                text = titleText,
                modifier = modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.API_CONFIG.path)
                }
            ) {
                Icon(
                    Icons.Filled.Cloud,
                    stringResource(R.string.api_config_icon_content_description)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.SETTINGS.path)
                }
            ) {
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
    AuthTopBar(
        navController = NavController(LocalContext.current)
    )
}