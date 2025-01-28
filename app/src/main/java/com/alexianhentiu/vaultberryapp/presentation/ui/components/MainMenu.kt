package com.alexianhentiu.vaultberryapp.presentation.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alexianhentiu.vaultberryapp.R

data class MenuItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun MainMenu(
    onDismissRequest: () -> Unit,
    expanded: Boolean,
    onLogout: () -> Unit,
    onAccountClick: () -> Unit
) {
    val menuItems = listOf(
        MenuItem(stringResource(R.string.account_menu_item_text), onAccountClick),
        MenuItem(stringResource(R.string.settings_menu_item_text), {}),
        MenuItem(stringResource(R.string.logout_menu_item_text), onLogout)
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        menuItems.forEach { menuItem ->
            DropdownMenuItem(
                text = { Text(menuItem.text) },
                onClick = {
                    onDismissRequest()
                    menuItem.onClick()
                }
            )
        }
    }
}
