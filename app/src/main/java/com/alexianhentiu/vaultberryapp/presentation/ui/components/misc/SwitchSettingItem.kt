package com.alexianhentiu.vaultberryapp.presentation.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchSettingItem(
    text: String,
    checked: Boolean,
    enabled: Boolean = true,
    fontSize: Int = 18,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        Switch(
            enabled = enabled,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SwitchSettingItemPreview() {
    SwitchSettingItem(
        text = "Dark theme",
        checked = true,
        onCheckedChange = {}
    )
}