package com.alexianhentiu.vaultberryapp.presentation.ui.components.misc

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.MenuItem

@Composable
fun MainMenu(
    onDismissRequest: () -> Unit,
    expanded: Boolean,
    onAccountClick: () -> Unit,
    onPasswordGeneratorClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val menuItems = listOf(
        MenuItem(
            stringResource(R.string.account_menu_item_text),
            onAccountClick,
            Icons.Filled.AccountCircle
        ),
        MenuItem(
            stringResource(R.string.password_generator_menu_item_text),
            onPasswordGeneratorClick,
            Icons.Filled.Password
        ),
        MenuItem(
            stringResource(R.string.settings_menu_item_text),
            onSettingsClick,
            Icons.Filled.Settings
        ),
        MenuItem(
            stringResource(R.string.about_menu_item_text),
            onAboutClick,
            Icons.Filled.Info
        )
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        menuItems.forEach { menuItem ->
            Row {
                DropdownMenuItem(
                    text = { Text(menuItem.text) },
                    onClick = {
                        onDismissRequest()
                        menuItem.onClick()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = menuItem.imageVector ?: Icons.Filled.Square,
                            contentDescription = menuItem.text
                        )
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMainMenu() {
    MainMenu(
        onDismissRequest = { },
        expanded = true,
        onAccountClick = { },
        onPasswordGeneratorClick = { },
        onSettingsClick = { },
        onAboutClick = { }
    )
}
