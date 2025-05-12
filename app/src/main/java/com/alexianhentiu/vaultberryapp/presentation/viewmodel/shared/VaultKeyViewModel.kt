package com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VaultKeyViewModel @Inject constructor() : ViewModel() {

    private var _decryptedKey: ByteArray? = null

    /**
     * Provides access to the decrypted vault key.
     * Returns null if the key has not been set or has been cleared.
     */
    val decryptedKey: ByteArray?
        get() = _decryptedKey?.copyOf()

    /**
     * Sets the decrypted master key. Should be called after successful decryption.
     * @param key The decrypted master key as a ByteArray.
     */
    fun setDecryptedKey(key: ByteArray) {
        _decryptedKey = key.copyOf()
    }

    /**
     * Clears the decrypted vault key from memory.
     * Should be called on user logout or when the session ends.
     */
    fun clearDecryptedKey() {
        _decryptedKey?.fill(0)
        _decryptedKey = null
    }

    /**
     * Called when the ViewModel is being destroyed.
     * Ensures the master key is cleared from memory.
     */
    override fun onCleared() {
        super.onCleared()
        clearDecryptedKey()
    }
}