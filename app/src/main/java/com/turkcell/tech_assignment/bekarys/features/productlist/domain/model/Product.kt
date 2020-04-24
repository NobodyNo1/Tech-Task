package com.turkcell.tech_assignment.bekarys.features.productlist.domain.model

import com.turkcell.tech_assignment.bekarys.features.common.model.ImageData

private const val DEFAULT_PRODUCT_ID: String = ""
private const val DEFAULT_NAME: String = ""
private const val DEFAULT_PRICE: Int = -1
private const val DEFAULT_IMAGE: String = ""
private const val DEFAULT_DESCRIPTION: String = ""
private val DEFAULT_LOCAL_IMAGE: ImageData = ImageData()

data class Product(
    val productId: String = DEFAULT_PRODUCT_ID,
    val name: String = DEFAULT_NAME,
    val price: Int = DEFAULT_PRICE,
    val imagePath: String = DEFAULT_IMAGE,
    val localImage: ImageData = DEFAULT_LOCAL_IMAGE,
    val description: String = DEFAULT_DESCRIPTION
)
