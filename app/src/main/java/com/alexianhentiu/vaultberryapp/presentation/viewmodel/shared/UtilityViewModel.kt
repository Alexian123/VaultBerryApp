package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.misc.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtilityViewModel @Inject constructor(
    private val copyToClipboardUseCase: CopyToClipboardUseCase,
): ViewModel() {

    fun copyToClipboard(text: String, label: String = "") {
        viewModelScope.launch {
            when (val result = copyToClipboardUseCase(text, label)) {
                is ActionResult.Success -> {
                    Log.d("UtilityViewModel", "Successfully copied to clipboard")
                }
                is ActionResult.Error -> {
                    Log.e(result.source, result.message)
                }
            }
        }
    }
}