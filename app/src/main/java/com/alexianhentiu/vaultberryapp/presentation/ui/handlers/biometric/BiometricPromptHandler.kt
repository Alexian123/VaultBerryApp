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
import com.alexianhentiu.vaultberryapp.presentation.ui.common.BiometricPromptRequest
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.BiometricViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.crypto.Cipher

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

    val authSuccessMsg = stringResource(R.string.auth_success_msg)
    val credentialsStoredMsg = stringResource(R.string.credentials_stored_msg)
    val biometricErrorMsg = stringResource(R.string.biometric_error)
    val credentialsClearedMsg = stringResource(R.string.credentials_cleared_msg)
    val authFailedMsg = stringResource(R.string.auth_failed_msg)
    val biometricAuthInitErrorMsg = stringResource(R.string.biometric_auth_init_error)

    LaunchedEffect(Unit) {
        biometricViewModel.biometricState.collectLatest { state ->
            when (state) {
                is BiometricState.Authenticated -> {
                    Toast.makeText(
                        context,
                        authSuccessMsg,
                        Toast.LENGTH_LONG
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.CredentialsStored -> {
                    Toast.makeText(context,
                        credentialsStoredMsg,
                        Toast.LENGTH_LONG
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.Error -> {
                    Toast.makeText(context,
                        "${biometricErrorMsg}${state.info.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    biometricViewModel.resetState()
                }
                is BiometricState.ClearedCredentials -> {
                    Toast.makeText(context,
                        credentialsClearedMsg,
                        Toast.LENGTH_LONG
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
                        biometricViewModel.cipherCache.setCipher(
                            request.context,
                            result.cryptoObject?.cipher
                        )
                        when (request) {
                            is BiometricPromptRequest.Store -> {
                                biometricViewModel.onBiometricStoreSuccess(request.context)
                            }

                            is BiometricPromptRequest.Retrieve -> {
                                biometricViewModel.onBiometricRetrieveSuccess(request.context)
                            }
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            context,
                            authFailedMsg,
                            Toast.LENGTH_LONG
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

            val cipher = biometricViewModel.cipherCache.getCipher(request.context) as? Cipher
            if (cipher != null) {
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            } else {
                biometricViewModel.onBiometricAuthError(biometricAuthInitErrorMsg)
            }
        }
    }
}
