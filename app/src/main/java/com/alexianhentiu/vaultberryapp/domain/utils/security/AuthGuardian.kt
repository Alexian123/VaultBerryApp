package com.alexianhentiu.vaultberryapp.domain.utils.security

import com.ongres.scram.client.ScramClient

class AuthGuardian {

    private lateinit var scramClient: ScramClient

    fun init(email: String, password: String) {
        scramClient = ScramClient.builder()
            .advertisedMechanisms(listOf("SCRAM-SHA-256"))
            .username(email)
            .password(password.toCharArray())
            .build()
    }

    fun getClientFirstMessage(): String {
        return scramClient.clientFirstMessage().toString()
    }

    fun getClientFinalMessage(serverFirstMessage: String): String {
        scramClient.serverFirstMessage(serverFirstMessage)
        return scramClient.clientFinalMessage().toString()
    }

    fun checkServerFinalMessage(clientFinalMessage: String) {
        scramClient.serverFinalMessage(clientFinalMessage)
    }
}