package com.turkcell.tech_assignment.bekarys.features.productlist.domain

import android.graphics.Bitmap
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductResponse
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import java.io.InputStream

interface ProductsRepository {

    fun getProducts(isLocalData: Boolean): ResponseData<List<Product>>

    fun getProduct(productId: String, isLocalData: Boolean): ResponseData<Product>

    fun downloadImage(url: String): ResponseData<InputStream>

    fun storeImage(filename: String, bitmap: Bitmap): ResponseData<String>

    fun isImageSaved(filename: String): ResponseData<Boolean>

    fun getImage(filename: String): ResponseData<String>

    fun updateLocalData(processedData: List<Product>)

    fun updateLocalProductData(processedData: Product)
}