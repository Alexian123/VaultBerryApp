package com.alexianhentiu.vaultberryapp.domain.utils.validation

import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType

interface InputValidator {
    fun getValidatorFunction(type: ValidatedFieldType): (String) -> Boolean
}