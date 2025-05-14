package com.example.watchfinder.api

import com.example.watchfinder.data.prefs.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AInterceptor(private val tokenM: TokenManager): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val isGcsUrl = url.contains("storage.googleapis.com")
        val builder = request.newBuilder()

        if (!isGcsUrl) {
            val token = tokenM.getToken()
            if (!token.isNullOrBlank()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(builder.build())
    }
}