package com.alexianhentiu.vaultberryapp.domain.model

data class DecryptedVaultKey(
    val key: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DecryptedVaultKey

        return key.contentEquals(other.key)
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }
}