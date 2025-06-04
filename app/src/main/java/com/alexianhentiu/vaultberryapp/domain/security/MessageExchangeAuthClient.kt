package com.alexianhentiu.vaultberryapp.domain.security

interface MessageExchangeAuthClient {

    fun init(email: String, password: String)

    fun getClientFirstMessage(): String

    fun getClientFinalMessage(serverFirstMessage: String): String

    fun checkServerFinalMessage(clientFinalMessage: String)
}