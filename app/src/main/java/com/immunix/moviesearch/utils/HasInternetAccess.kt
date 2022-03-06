package com.immunix.moviesearch.utils

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object HasInternetAccess {
    fun execute(): Boolean {
        return try {
            val timeoutMs = 1500
            val socket = Socket()
            val address = InetSocketAddress("8.8.8.8", 53)

            socket.connect(address, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}