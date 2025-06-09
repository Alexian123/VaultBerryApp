package com.alexianhentiu.vaultberryapp.domain.model

data class TotpData(
    val secret: String,
    val qrCodeBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TotpData

        if (secret != other.secret) return false
        if (!qrCodeBytes.contentEquals(other.qrCodeBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secret.hashCode()
        result = 31 * result + qrCodeBytes.contentHashCode()
        return result
    }
}
