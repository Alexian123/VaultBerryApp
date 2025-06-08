package com.alexianhentiu.vaultberryapp.presentation.ui.screens.apiConfig

sealed class PingState {
    data object Idle : PingState()
    data object Loading : PingState()
    data class Success(val host: String, val port: Int) : PingState()
    data class Failure(val message: String) : PingState()
}