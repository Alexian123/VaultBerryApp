package com.alexianhentiu.vaultberryapp.presentation.ui.components.items

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R

@Composable
fun ExpandableSectionItem(
    title: String,
    onExpand: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
            .clickable {
                isExpanded = !isExpanded
                onExpand()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.9f)
            )
            Icon(
                imageVector = if (isExpanded)
                    Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(
                    R.string.toggle_section_expansion_action_content_description
                ),
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExpandableSectionItemPreview() {
    ExpandableSectionItem(
        title = "Section 1"
    )
}