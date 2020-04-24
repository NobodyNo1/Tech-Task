package com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network

import com.google.gson.annotations.SerializedName
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductModel

data class ProductResponse(
    @SerializedName("products")
    val products: List<ProductModel> = emptyList()
)