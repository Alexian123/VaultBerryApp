package com.alexianhentiu.vaultberryapp.data.validation

import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.domain.validation.InputValidator

class LaxInputValidator : InputValidator {
    override fun getValidatorFunction(
        taype: InputType
    ): (String) -> Boolean {
        return String::isNotBlank
    }
}