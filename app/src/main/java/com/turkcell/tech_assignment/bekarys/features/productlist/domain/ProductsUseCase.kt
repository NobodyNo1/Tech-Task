package com.turkcell.tech_assignment.bekarys.features.productlist.domain

import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData

interface ProductsUseCase {

    fun getProducts(
        fromCache: Boolean = true
    ): ResponseData<List<Product>>
}