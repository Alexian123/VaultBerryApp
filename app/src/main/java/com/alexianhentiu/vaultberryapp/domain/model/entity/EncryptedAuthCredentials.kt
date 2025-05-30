package com.alexianhentiu.vaultberryapp.domain.model.entity

data class EncryptedAuthCredentials(
    val encryptedPassword: ByteArray,
    val passwordIv: ByteArray,
    val email: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedAuthCredentials

        if (!encryptedPassword.contentEquals(other.encryptedPassword)) return false
        if (!passwordIv.contentEquals(other.passwordIv)) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encryptedPassword.contentHashCode()
        result = 31 * result + passwordIv.contentHashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}