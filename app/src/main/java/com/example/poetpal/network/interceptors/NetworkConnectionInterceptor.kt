package com.example.poetpal.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class NetworkConnectionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.run {
            if (!isConnected(context = context)) {
                Log.i("retrofit", "there is no connection")
                throw IOException()
            } else {
                val builder = chain.request().newBuilder()
                return@run chain.proceed(builder.build())
            }
        }

    private fun isConnected(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result =
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

        return result
    }
}
