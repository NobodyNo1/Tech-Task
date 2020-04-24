package com.turkcell.tech_assignment.bekarys.features.common.exceptions

sealed class BasicNetworkException(
    message: String
) : NetworkException(message) {

    //status code 4**
    object ClientError : BasicNetworkException("Client error")

    //status code 5**
    object ServerError : BasicNetworkException("Server error")

}