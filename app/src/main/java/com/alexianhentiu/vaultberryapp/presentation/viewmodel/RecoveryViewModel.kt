package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.alexianhentiu.vaultberryapp.domain.model.Account
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.main.state.RecoveryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RecoveryViewModel @Inject constructor(
    val inputValidator: InputValidator
) : ViewModel() {

    private val _recoveryState = MutableStateFlow<RecoveryState>(RecoveryState.Idle)
    val recoveryState: StateFlow<RecoveryState> = _recoveryState

    private val _account = MutableStateFlow<Account?>(null)
}