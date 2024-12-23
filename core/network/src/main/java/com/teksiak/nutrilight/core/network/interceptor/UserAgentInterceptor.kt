package com.teksiak.nutrilight.core.network.interceptor

import android.content.Context
import android.content.pm.PackageManager
import com.teksiak.nutrilight.core.network.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor(
    private val context: Context,
): Interceptor {

    private val userAgent: String by lazy {
        createUserAgent()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(request)
    }

    private fun createUserAgent(): String {
        with(context.packageManager) {
            val versionName = try {
                getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                "unknown"
            }

            val applicationName = getApplicationLabel(context.applicationInfo)

            return "$applicationName/$versionName (${NetworkConstants.CONTACT_EMAIL})"
        }
    }

}