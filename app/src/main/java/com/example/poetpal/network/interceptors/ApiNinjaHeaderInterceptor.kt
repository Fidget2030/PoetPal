package com.example.poetpal.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

private const val APININJA_KEY = "incJFBjor+LpLeAGACn/Dw==5cZe8JdapphNYOH8"

class ApiNinjaHeaderInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.run {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("X-Api-Key", APININJA_KEY)
            return@run chain.proceed(requestBuilder.build())
        }
}
