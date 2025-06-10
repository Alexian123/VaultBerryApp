package com.alexianhentiu.vaultberryapp.domain.utils

import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType

interface InputValidator {
    fun getValidatorFunction(type: InputType): (String) -> Boolean
}