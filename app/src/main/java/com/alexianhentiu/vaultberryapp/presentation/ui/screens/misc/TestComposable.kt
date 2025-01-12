package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableList(items: List<ExpandableItem>) {
    val expandedStates by remember {
        mutableStateOf(items.associate { it.id to false }.toMutableMap())
    }

    Column {
        items.forEach { item ->
            ExpandableItemView(
                item = item,
                isExpanded = expandedStates[item.id] ?: false,
                onToggle = {
                    expandedStates[item.id] = !(expandedStates[item.id] ?: false)
                }
            )
        }
    }
}

@Composable
fun ExpandableItemView(
    item: ExpandableItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onToggle() }
    ) {
        Column {
            Text(
                text = item.title,
                modifier = Modifier.padding(8.dp)
            )
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
            ) {
                Text(
                    text = item.content,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

data class ExpandableItem(val id: Int, val title: String, val content: String)

@Preview(showBackground = true)
@Composable
fun PreviewExpandableList() {
    val items = listOf(
        ExpandableItem(1, "Item 1", "Content for item 1"),
        ExpandableItem(2, "Item 2", "Content for item 2"),
        ExpandableItem(3, "Item 3", "Content for item 3")
    )
    ExpandableList(items = items)
}