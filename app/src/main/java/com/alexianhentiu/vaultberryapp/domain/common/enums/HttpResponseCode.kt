package com.alexianhentiu.vaultberryapp.domain.common.enums

enum class HttpResponseCode(val code: Int) {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403)
}