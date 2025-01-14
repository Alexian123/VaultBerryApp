package com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.alexianhentiu.vaultberryapp.R

@Composable
fun ToggleVisibilityButton(
    onVisibilityChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            visible = !visible
            onVisibilityChanged(visible)
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = stringResource(
                id = if (visible) R.string.hide_content_description else R.string.show_content_description
            )
        )
    }
}