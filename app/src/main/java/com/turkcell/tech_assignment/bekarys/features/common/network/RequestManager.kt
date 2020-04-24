package com.turkcell.tech_assignment.bekarys.features.common.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.BasicNetworkException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.NetworkException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.ConnectionException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.EmptyResponseBodyException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.ResponseProcessError
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody

private const val EMPTY_JSON: String = "{}"

class RequestManager(
    protected val client: OkHttpClient,
    protected val requestBuilder: Request.Builder,
    protected val gson: Gson,
    protected val connectionManager: NetworkStateManager
) {

    fun <T> getMethod(
        url: String,
        clazz: Class<T>
    ): ResponseData<T> {
        val response = getRawBody(url)
        if (response is ResponseData.Success) {
            return try {
                val getMethodResponseData = gson.fromJson(
                    response.data.string() ?: EMPTY_JSON,
                    clazz
                )

                ResponseData.Success(
                    getMethodResponseData
                )
            } catch (ex: JsonSyntaxException) {
                ResponseData.Fail(ResponseProcessError("The response scheme has changed"))
            }
        }
        if (response !is ResponseData.Fail)
            return ResponseData.Fail(IllegalStateException())
        return ResponseData.Fail(response.exception)
    }

    fun getRawBody(
        url: String
    ): ResponseData<ResponseBody> {
        if (!connectionManager.isOnline())
            return ResponseData.Fail(ConnectionException.NoConnection)

        val getMethodRequest = requestBuilder.url(url).build()
        try {
            val response = client.newCall(getMethodRequest).execute()
            if (!response.isSuccessful) {

                return ResponseData.Fail(
                    IllegalStateException()
                )
            }

            val data = response.body() ?: return ResponseData.Fail(
                EmptyResponseBodyException(
                    ""
                )
            )
            return ResponseData.Success(
                data
            )
        } catch (ex: Exception) {

            return ResponseData.Fail(ex)
        }
    }
}
