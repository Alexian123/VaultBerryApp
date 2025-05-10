package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.utility.GetValidatorUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.utils.types.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ValidatedFieldType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
    private val evalPasswordStrengthUseCase: EvalPasswordStrengthUseCase,
    private val getValidatorUseCase: GetValidatorUseCase
): ViewModel() {

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

    fun getFieldValidator(type: ValidatedFieldType): (String) -> Boolean {
        when (val result = getValidatorUseCase(type)) {
            is UseCaseResult.Success -> {
                return result.data
            }
            is UseCaseResult.Error -> {
                Log.e(result.source, result.message)
                return { false }
            }
        }
    }
}