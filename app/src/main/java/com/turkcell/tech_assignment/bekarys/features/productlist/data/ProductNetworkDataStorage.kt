package com.turkcell.tech_assignment.bekarys.features.productlist.data

import com.turkcell.tech_assignment.bekarys.features.common.network.RequestManager
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductModel
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductResponse
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import java.io.InputStream

private const val API_PRODUCT_LIST =
    "https://s3-eu-west-1.amazonaws.com/developer-application-test/cart/list"

private const val API_PRODUCT_DETAILS =
    "https://s3-eu-west-1.amazonaws.com/developer-application-test/cart/%s/detail"

class ProductNetworkDataStorage(
    private val requestManager: RequestManager
) {

    fun getProducts(): ResponseData<ProductResponse> =
        requestManager.getMethod(API_PRODUCT_LIST, ProductResponse::class.java)

    fun getProduct(productId: String): ResponseData<ProductModel> =
        requestManager.getMethod(API_PRODUCT_DETAILS.format(productId), ProductModel::class.java)

    fun getImage(imagePath: String): ResponseData<InputStream> {
        val rawBody = requestManager.getRawBody(imagePath)
        if (rawBody is ResponseData.Fail)
            return ResponseData.Fail(rawBody.exception)
        if (rawBody !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())

        return ResponseData.Success(rawBody.data.byteStream())
    }
}