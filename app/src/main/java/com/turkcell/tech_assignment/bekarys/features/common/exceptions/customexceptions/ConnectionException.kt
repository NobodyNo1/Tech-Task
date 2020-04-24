package com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions

import com.turkcell.tech_assignment.bekarys.features.common.exceptions.NetworkException

sealed class ConnectionException(
    message: String
) : NetworkException(message) {

    object NoConnection : ConnectionException("No connection")
}