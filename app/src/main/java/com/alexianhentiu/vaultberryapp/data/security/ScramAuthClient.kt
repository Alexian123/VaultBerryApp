package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.domain.security.MessageExchangeAuthClient
import com.ongres.scram.client.ScramClient

class ScramAuthClient : MessageExchangeAuthClient {

    private lateinit var scramClient: ScramClient

    override fun init(email: String, password: String) {
        scramClient = ScramClient.builder()
            .advertisedMechanisms(listOf("SCRAM-SHA-256"))
            .username(email)
            .password(password.toCharArray())
            .build()
    }

    override fun getClientFirstMessage(): String {
        return scramClient.clientFirstMessage().toString()
    }

    override fun getClientFinalMessage(serverFirstMessage: String): String {
        scramClient.serverFirstMessage(serverFirstMessage)
        return scramClient.clientFinalMessage().toString()
    }

    override fun checkServerFinalMessage(clientFinalMessage: String) {
        scramClient.serverFinalMessage(clientFinalMessage)
    }
}