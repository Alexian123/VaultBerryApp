package com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetAppInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetValidatorFunctionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
    private val evalPasswordStrengthUseCase: EvalPasswordStrengthUseCase,
    private val getValidatorFunctionUseCase: GetValidatorFunctionUseCase,
    private val generatePasswordUseCase: GeneratePasswordUseCase,
    private val getAppInfoUseCase: GetAppInfoUseCase
): ViewModel() {

    private val _errorInfo = MutableSharedFlow<ErrorInfo>()
    val errorInfo: SharedFlow<ErrorInfo> = _errorInfo.asSharedFlow()

    fun getAppInfo(info: AppInfo): String {
        return when (val result = getAppInfoUseCase(info)) {
            is UseCaseResult.Success -> {
                result.data
            }
            is UseCaseResult.Error -> "N/A"
        }
    }

    fun getValidatorFunction(type: InputType, lax: Boolean): (String) -> Boolean {
        return when (val result = getValidatorFunctionUseCase(type, lax)) {
            is UseCaseResult.Success -> {
                result.data
            }

            is UseCaseResult.Error -> {
                { true }
            }
        }
    }

    fun copyToClipboard(text: String, label: String = "") {
        viewModelScope.launch {
            when (val result = copyToClipboardUseCase(text, label)) {
                is UseCaseResult.Success -> {}
                is UseCaseResult.Error -> _errorInfo.emit(result.info)
            }
        }
    }

    fun evalPasswordStrength(password: String): PasswordStrength {
        return when (val result = evalPasswordStrengthUseCase(password)) {
            is UseCaseResult.Success -> {
                result.data
            }

            is UseCaseResult.Error -> {
                PasswordStrength.NONE
            }
        }
    }

    fun generatePassword(options: PasswordGenOptions): String {
        return when (val result = generatePasswordUseCase(options)) {
            is UseCaseResult.Success -> {
                result.data
            }

            is UseCaseResult.Error -> {
                ""
            }
        }
    }
}