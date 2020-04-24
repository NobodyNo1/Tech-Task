package com.turkcell.tech_assignment.bekarys.features.common.network

import android.content.Context
import android.net.ConnectivityManager

class DefaultNetworkStateManager(
    private val context: Context
) : NetworkStateManager {

    override fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager
        return connectivityManager?.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo()?.isConnected() == true
    }
}