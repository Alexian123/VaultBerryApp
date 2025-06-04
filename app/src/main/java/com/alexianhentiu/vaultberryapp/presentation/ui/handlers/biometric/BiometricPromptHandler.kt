package com.alexianhentiu.vaultberryapp.presentation.ui.handlers.biometric

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.application.usecase.biometric.BiometricPromptRequest
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.BiometricViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * A Composable that observes the BiometricViewModel's requests to show a biometric prompt
 * and directly handles the creation and display of the BiometricPrompt using the FragmentActivity context.
 */
@Composable
fun BiometricPromptHandler(
    fragmentActivity: FragmentActivity,
    biometricViewModel: BiometricViewModel
) {
    val context = LocalContext.current

    val storeTitle = stringResource(R.string.biometric_store_title)
    val storeSubtitle = stringResource(R.string.biometric_store_subtitle)
    val retrieveTitle = stringResource(R.string.biometric_retrieve_title)
    val retrieveSubtitle = stringResource(R.string.biometric_retrieve_subtitle)

    LaunchedEffect(Unit) {
        biometricViewModel.biometricState.collectLatest { state ->
            when (state) {
                is BiometricState.Authenticated -> {
                    Toast.makeText(
                        context,
                        "Authentication successful!",
                        Toast.LENGTH_LONG
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.CredentialsStored -> {
                    Toast.makeText(context,
                        "Credentials stored successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.Error -> {
                    Toast.makeText(context,
                        "Biometric Error: ${state.info.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.ClearedCredentials -> {
                    Toast.makeText(context,
                        "Credentials cleared successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    biometricViewModel.resetState()
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        biometricViewModel.startBiometricPrompt.collectLatest { request ->
            val executor = ContextCompat.getMainExecutor(fragmentActivity)
            val biometricPrompt = BiometricPrompt(
                fragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        biometricViewModel.onBiometricAuthError(errString.toString())
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        when (request) {
                            is BiometricPromptRequest.Store ->
                                biometricViewModel.onBiometricStoreSuccess(result)
                            is BiometricPromptRequest.Retrieve ->
                                biometricViewModel.onBiometricRetrieveSuccess(result)
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            context,
                            "Authentication failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            val title = when (request) {
                is BiometricPromptRequest.Store -> storeTitle
                is BiometricPromptRequest.Retrieve -> retrieveTitle
            }
            val subtitle = when (request) {
                is BiometricPromptRequest.Store -> storeSubtitle
                is BiometricPromptRequest.Retrieve -> retrieveSubtitle
            }
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText("Cancel")
                .build()

            val cryptoObject = request.cryptoObject
            if (cryptoObject != null) {
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cryptoObject))
            } else {
                biometricViewModel.onBiometricAuthError(
                    "Biometric authentication could not be initiated."
                )
            }
        }
    }
}
