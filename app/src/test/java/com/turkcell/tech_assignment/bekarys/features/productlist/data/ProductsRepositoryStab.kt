package com.turkcell.tech_assignment.bekarys.features.productlist.data

import android.graphics.Bitmap
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsRepository
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import java.io.InputStream

class ProductsRepositoryStab : ProductsRepository {

    lateinit var responseForGetProductsFromCache: ResponseData<List<Product>>
    lateinit var responseForGetProductsFromNetwork: ResponseData<List<Product>>
    lateinit var responseForGetProduct: ResponseData<Product>
    lateinit var responseForDownloadImage: ResponseData<InputStream>
    lateinit var responseForStoreImage: ResponseData<String>
    lateinit var responseForIsImageSaved: ResponseData<Boolean>
    lateinit var responseForGetImage: ResponseData<String>

    override fun getProducts(isLocalData: Boolean): ResponseData<List<Product>> =
        if (isLocalData)responseForGetProductsFromCache else responseForGetProductsFromNetwork

    override fun getProduct(productId: String, isLocalData: Boolean): ResponseData<Product> =
        responseForGetProduct

    override fun downloadImage(url: String): ResponseData<InputStream> = responseForDownloadImage

    override fun storeImage(filename: String, bitmap: Bitmap): ResponseData<String> =
        responseForStoreImage

    override fun isImageSaved(filename: String): ResponseData<Boolean> = responseForIsImageSaved

    override fun getImage(filename: String): ResponseData<String> = responseForGetImage

    override fun updateLocalData(processedData: List<Product>) = Unit

    override fun updateLocalProductData(processedData: Product) = Unit
}