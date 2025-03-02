package com.alexianhentiu.vaultberryapp.presentation.ui.screens.extra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R

@Composable
fun UnlockVaultScreen(
    onUnlock: () -> Unit,
) {
    Scaffold { contentPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .clickable { onUnlock() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .align(Alignment.Center),
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.unlock_vault_icon_content_description),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
                Text(
                    text = stringResource(R.string.unlock_vault_text),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnlockVaultScreenPreview() {
    UnlockVaultScreen(onUnlock = {})
}