package com.turkcell.tech_assignment.bekarys.features.productlist.data

import android.graphics.Bitmap
import com.turkcell.tech_assignment.bekarys.features.common.model.ImageData
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local.ProductEntry
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductModel
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.network.ProductResponse
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsRepository
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import java.io.InputStream

class DefaultProductRepository(
    private val localDataStorage: ProductLocalDataStorage,
    private val networkDataStorage: ProductNetworkDataStorage
) : ProductsRepository {

    //todo: in ram cache logic

    override fun getProducts(isLocalData: Boolean): ResponseData<List<Product>> {
        if (!isLocalData) {
            return unwrapNetworkData(networkDataStorage.getProducts())
        }
        return unwrapLocalData(localDataStorage.getProducts())
    }

    override fun getProduct(productId: String, isLocalData: Boolean): ResponseData<Product> {
        if (!isLocalData) {
            return unwrapProductNetworkData(networkDataStorage.getProduct(productId))
        }
        return unwrapProductLocalData(localDataStorage.getProduct(productId))
    }

    private fun unwrapProductLocalData(productResponse: ResponseData<ProductEntry>): ResponseData<Product> {
        if (productResponse is ResponseData.Fail)
            return ResponseData.Fail(productResponse.exception)
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())

        val product = Product(
            productId = productResponse.data.productId,
            price = productResponse.data.price,
            imagePath = productResponse.data.imagePath,
            name = productResponse.data.name,
            description = productResponse.data.description,
            localImage = ImageData(productResponse.data.localOriginalImagePath, productResponse.data.localSmallImagePath)
        )

        return ResponseData.Success(product)
    }

    private fun unwrapProductNetworkData(productResponse: ResponseData<ProductModel>): ResponseData<Product> {
        if (productResponse is ResponseData.Fail)
            return ResponseData.Fail(productResponse.exception)
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())

        val product = Product(
            productId = productResponse.data.productId,
            price = productResponse.data.price,
            imagePath = productResponse.data.image,
            name = productResponse.data.name,
            description = productResponse.data.description
        )

        return ResponseData.Success(product)
    }

    private fun unwrapNetworkData(productResponse: ResponseData<ProductResponse>): ResponseData<List<Product>> {
        if (productResponse is ResponseData.Fail)
            return ResponseData.Fail(productResponse.exception)
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())

        val productList = productResponse.data.products.map {
            Product(
                productId = it.productId,
                price = it.price,
                imagePath = it.image,
                name = it.name
            )
        }
        return ResponseData.Success(productList)
    }

    private fun unwrapLocalData(productResponse: ResponseData<List<ProductEntry>>): ResponseData<List<Product>> {
        if (productResponse is ResponseData.Fail)
            return ResponseData.Fail(productResponse.exception)
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())

        val productList = productResponse.data.map {
            Product(
                productId = it.productId,
                price = it.price,
                imagePath = it.imagePath,
                name = it.name,
                localImage = ImageData(it.localOriginalImagePath, it.localSmallImagePath)
            )
        }
        return ResponseData.Success(productList)
    }

    override fun downloadImage(url: String): ResponseData<InputStream> =
        networkDataStorage.getImage(url)

    override fun storeImage(filename: String, bitmap: Bitmap) =
        localDataStorage.saveImage(filename, bitmap)

    override fun isImageSaved(filename: String): ResponseData<Boolean> =
        localDataStorage.isImageSaved(filename)

    override fun getImage(filename: String) = localDataStorage.getImage(filename)

    override fun updateLocalData(processedData: List<Product>) {
        localDataStorage.updateProducts(processedData)
    }

    override fun updateLocalProductData(processedData: Product) {
        localDataStorage.updateProduct(processedData)
    }

}