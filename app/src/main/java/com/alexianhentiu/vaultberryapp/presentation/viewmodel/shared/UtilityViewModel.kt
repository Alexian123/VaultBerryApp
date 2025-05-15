package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.GetValidatorUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
    private val evalPasswordStrengthUseCase: EvalPasswordStrengthUseCase,
    private val getValidatorUseCase: GetValidatorUseCase
): ViewModel() {

    private val _inputValidator = MutableStateFlow<InputValidator?>(null)
    val inputValidator: StateFlow<InputValidator?> = _inputValidator

    init {
        viewModelScope.launch {
            when (val result = getValidatorUseCase()) {
                is UseCaseResult.Success -> {
                    _inputValidator.value = result.data
                    Log.d("UtilityViewModel", "Successfully loaded ${result.data::class}")
                }
                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _inputValidator.value = null
                }
            }
        }
    }

    fun copyToClipboard(text: String, label: String = "") {
        when (val result = copyToClipboardUseCase(text, label)) {
            is UseCaseResult.Success -> {
                Log.d("UtilityViewModel", "Successfully copied to clipboard")
            }
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
            }
        }
    }

    fun evalPasswordStrength(password: String): PasswordStrength {
        when (val result = evalPasswordStrengthUseCase(password)) {
            is UseCaseResult.Success -> {
                return result.data
            }
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                return PasswordStrength.NONE
            }
        }
    }
}