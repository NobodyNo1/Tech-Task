package com.turkcell.tech_assignment.bekarys.features.common.network

class TestNetworkStateManager : NetworkStateManager {

    var isNetworkAvailable: Boolean = false

    override fun isOnline(): Boolean = isNetworkAvailable

}