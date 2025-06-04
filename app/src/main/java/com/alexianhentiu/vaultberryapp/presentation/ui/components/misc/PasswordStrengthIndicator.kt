package com.alexianhentiu.vaultberryapp.presentation.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {
    val gradientColors = when (strength) {
        PasswordStrength.WEAK -> listOf(Color.Red, Color(0xFFFFA726)) // Red to Orange
        PasswordStrength.AVERAGE -> listOf(Color(0xFFFFA726), Color.Yellow) // Orange to Yellow
        PasswordStrength.STRONG -> listOf(Color.Yellow, Color.Green) // Yellow to Green
        PasswordStrength.VERY_STRONG -> listOf(Color.Green, Color(0xFF006400)) // Green to Dark Green
        PasswordStrength.NONE -> listOf(Color.Transparent, Color.Transparent) // Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Brush.horizontalGradient(gradientColors))
    )
}

@Composable
@Preview(showBackground = true)
fun PasswordStrengthIndicatorPreview() {
    PasswordStrengthIndicator(strength = PasswordStrength.STRONG)
}