package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.SwitchSettingItem

@Composable
fun SettingsForm(
    useSystemTheme: Boolean,
    darkTheme: Boolean,
    debugMode: Boolean,
    biometricEnabled: Boolean,
    onUseSystemThemeChange: (Boolean) -> Unit,
    onDarkThemeChange: (Boolean) -> Unit,
    onDebugModeChange: (Boolean) -> Unit,
    onAutofillActivation: () -> Unit,
    onBiometricEnabledChange: (Boolean) -> Unit,
    onClearBiometricData: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.appearance_settings_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SwitchSettingItem(
            text = stringResource(R.string.use_system_theme_setting_text),
            checked = useSystemTheme,
            onCheckedChange = onUseSystemThemeChange
        )
        SwitchSettingItem(
            enabled = !useSystemTheme,
            text = stringResource(R.string.dark_theme_setting_text),
            checked = darkTheme,
            onCheckedChange = onDarkThemeChange
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = stringResource(R.string.security_settings_text),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        SwitchSettingItem(
            text = stringResource(R.string.biometric_login_setting_text),
            checked = biometricEnabled,
            onCheckedChange = onBiometricEnabledChange
        )
        TextButton(
            onClick = onClearBiometricData,
        ) {
            Text(
                text = stringResource(R.string.clear_biometric_data_text),
                fontSize = 14.sp
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = stringResource(R.string.autofill_settings_text),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        TextButton(
            onClick = onAutofillActivation,
        ) {
            Text(
                text = stringResource(R.string.autofill_activation_text),
                fontSize = 14.sp
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = stringResource(R.string.advanced_settings_text),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )
        SwitchSettingItem(
            text = stringResource(R.string.debug_mode_setting_text),
            checked = debugMode,
            onCheckedChange = onDebugModeChange
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsFormPreview() {
    SettingsForm(
        useSystemTheme = false,
        darkTheme = true,
        debugMode = true,
        biometricEnabled = true,
        onUseSystemThemeChange = {},
        onDarkThemeChange = {},
        onDebugModeChange = {},
        onAutofillActivation = {},
        onBiometricEnabledChange = {},
        onClearBiometricData = {}
    )
}