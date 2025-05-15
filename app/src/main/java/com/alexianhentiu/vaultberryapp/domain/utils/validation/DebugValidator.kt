package com.alexianhentiu.vaultberryapp.domain.utils.validation

import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType

class DebugValidator : InputValidator {
    override fun getValidatorFunction(
        taype: ValidatedFieldType
    ): (String) -> Boolean {
        return String::isNotBlank
    }
}