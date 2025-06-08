package com.alexianhentiu.vaultberryapp.data.util

import com.alexianhentiu.vaultberryapp.domain.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

class NetworkUtilsImpl : NetworkUtils {
    override suspend fun isHostReachable(
        host: String,
        port: Int,
        timeoutMs: Int
    ): Boolean {
        return withContext(Dispatchers.IO) {
            var socket: Socket? = null
            try {
                socket = Socket()
                val socketAddress = InetSocketAddress(host, port)
                socket.connect(socketAddress, timeoutMs)
                true // Connection successful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                try {
                    socket?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}