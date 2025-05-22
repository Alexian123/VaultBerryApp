package com.alexianhentiu.vaultberryapp.presentation.utils.store

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val text: String,
    val onClick: () -> Unit,
    val imageVector: ImageVector? = null
)