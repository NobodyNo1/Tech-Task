package com.turkcell.tech_assignment.bekarys.features.productdetails.domain

import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData

interface ProductDetailsUseCase {

    fun getProductDetails(
        productId: String,
        fromCache: Boolean
    ): ResponseData<Product>
}