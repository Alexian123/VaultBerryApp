package com.alexianhentiu.vaultberryapp.domain.validation

import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType

interface InputValidator {
    fun getValidatorFunction(type: InputType): (String) -> Boolean
}