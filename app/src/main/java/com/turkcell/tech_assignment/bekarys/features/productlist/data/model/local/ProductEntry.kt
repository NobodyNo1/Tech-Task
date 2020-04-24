package com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.turkcell.tech_assignment.bekarys.features.common.model.ImageData

private const val DEFAULT_PRODUCT_ID: String = ""
private const val DEFAULT_NAME: String = ""
private const val DEFAULT_PRICE: Int = -1
private const val DEFAULT_IMAGE: String = ""
private const val DEFAULT_DESCRIPTION: String = ""
private const val DEFAULT_ID: Int = 0

@Entity(tableName = "products")
data class ProductEntry(
    @PrimaryKey
    val productId: String = DEFAULT_PRODUCT_ID,
    val name: String = DEFAULT_NAME,
    val price: Int = DEFAULT_PRICE,
    val imagePath: String = DEFAULT_IMAGE,
    val localOriginalImagePath: String = DEFAULT_IMAGE,
    val localSmallImagePath: String = DEFAULT_IMAGE,
    val description: String = DEFAULT_DESCRIPTION
)