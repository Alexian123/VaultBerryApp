package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.GetValidatorUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.ErrorInfo
import com.alexianhentiu.vaultberryapp.presentation.utils.containers.PasswordGenOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
    private val evalPasswordStrengthUseCase: EvalPasswordStrengthUseCase,
    private val getValidatorUseCase: GetValidatorUseCase,
    private val generatePasswordUseCase: GeneratePasswordUseCase
): ViewModel() {

    private val _inputValidator = MutableStateFlow<InputValidator?>(null)
    val inputValidator: StateFlow<InputValidator?> = _inputValidator

    private val _errorInfo = MutableSharedFlow<ErrorInfo>()
    val errorInfo: SharedFlow<ErrorInfo> = _errorInfo.asSharedFlow()

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
                    _errorInfo.emit(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun copyToClipboard(text: String, label: String = "") {
        viewModelScope.launch {
            when (val result = copyToClipboardUseCase(text, label)) {
                is UseCaseResult.Success -> {
                    Log.d("UtilityViewModel", "Successfully copied to clipboard")
                }

                is UseCaseResult.Error -> {
                    Log.e(result.source, result.message)
                    _errorInfo.emit(
                        ErrorInfo(
                            type = result.type,
                            source = result.source,
                            message = result.message
                        )
                    )
                }
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

    fun generatePassword(options: PasswordGenOptions): String {
        when (val result = generatePasswordUseCase(
            length = options.length,
            includeUppercase = options.includeUppercase,
            includeNumbers = options.includeNumbers,
            includeSpecialChars = options.includeSpecialChars,
            includeSpaces = options.includeSpaces
        )) {
            is UseCaseResult.Success -> {
                return result.data
            }
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                return ""
            }
        }
    }
}