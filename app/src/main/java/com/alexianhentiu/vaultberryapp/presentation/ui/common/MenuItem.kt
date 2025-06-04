package com.alexianhentiu.vaultberryapp.presentation.ui.common

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val text: String,
    val onClick: () -> Unit,
    val imageVector: ImageVector? = null
)