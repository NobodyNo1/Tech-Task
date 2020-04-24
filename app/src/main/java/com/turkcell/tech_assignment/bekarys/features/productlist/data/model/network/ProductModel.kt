package com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network

import com.google.gson.annotations.SerializedName

private const val DEFAULT_PRODUCT_ID: String = ""
private const val DEFAULT_NAME: String = ""
private const val DEFAULT_PRICE: Int = -1
private const val DEFAULT_IMAGE: String = ""
private const val DEFAULT_DESCRIPTION: String = ""

data class ProductModel(
    @SerializedName("product_id")
    val productId: String = DEFAULT_PRODUCT_ID,
    @SerializedName("name")
    val name: String = DEFAULT_NAME,
    @SerializedName("price")
    val price: Int = DEFAULT_PRICE,
    @SerializedName("image")
    val image: String = DEFAULT_IMAGE,
    @SerializedName("description")
    val description: String = DEFAULT_DESCRIPTION
)