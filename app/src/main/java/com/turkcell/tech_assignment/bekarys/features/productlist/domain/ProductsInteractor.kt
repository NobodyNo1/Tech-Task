package com.turkcell.tech_assignment.bekarys.features.productlist.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.ConnectionException
import com.turkcell.tech_assignment.bekarys.features.common.network.NetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.EmptyDataException
import com.turkcell.tech_assignment.bekarys.features.common.model.ImageData
import com.turkcell.tech_assignment.bekarys.features.productdetails.domain.ProductDetailsUseCase
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData

private const val IMAGE_IMAGE_FORMAT = "image_%s_%s"
private const val ORIGINAL_SIZE_TYPE = "original"
private const val SMALL_SIZE_TYPE = "small"

class ProductsInteractor(
    private val networkStateManager: NetworkStateManager,
    private val productsRepository: ProductsRepository
) : ProductsUseCase, ProductDetailsUseCase {

    override fun getProductDetails(productId: String, fromCache: Boolean): ResponseData<Product> {
        if (!networkStateManager.isOnline() && !fromCache) {
            return ResponseData.Fail(ConnectionException.NoConnection)
        }
        val isLocalData = fromCache
        var productResponse = productsRepository.getProduct(productId, fromCache)
        if (isLocalData && productResponse is ResponseData.Success && productResponse.data.description.isNotEmpty()) {
            return ResponseData.Success(processData(productResponse.data))
        }
        //If there is no description in local data then load from network
        if (isLocalData && productResponse is ResponseData.Success && productResponse.data.description.isEmpty())
            productResponse = productsRepository.getProduct(productId, false)
        if (productResponse is ResponseData.Fail) {
            return ResponseData.Fail(productResponse.exception)
        }
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())
        val product = productResponse.data
        val processedData = processData(product)
        updateLocalProductData(processedData)
        return ResponseData.Success(processedData)
    }

    override fun getProducts(fromCache: Boolean): ResponseData<List<Product>> {
        if (!networkStateManager.isOnline() && !fromCache) {
            return ResponseData.Fail(ConnectionException.NoConnection)
        }
        val isLocalData = fromCache

        var productResponse = productsRepository.getProducts(fromCache)
        if (isLocalData && productResponse is ResponseData.Success && productResponse.data.isNotEmpty()) {
            return ResponseData.Success(processDataList(productResponse.data))
        }
        //If there is no data in database, but network is available
        if (isLocalData && productResponse is ResponseData.Success && productResponse.data.isEmpty())
            productResponse = productsRepository.getProducts(false)

        if (productResponse is ResponseData.Fail) {
            return ResponseData.Fail(productResponse.exception)
        }
        if (productResponse !is ResponseData.Success)
            return ResponseData.Fail(IllegalStateException())
        if (productResponse.data.isEmpty())
            return ResponseData.Fail(EmptyDataException("The products are empty"))
        val productList = productResponse.data
        val processedData = processDataList(productList)
        updateLocalDataStorage(processedData)
        return ResponseData.Success(processedData)
    }

    private fun updateLocalDataStorage(processedData: List<Product>) {
        productsRepository.updateLocalData(processedData)
    }

    private fun updateLocalProductData(processedData: Product) {
        productsRepository.updateLocalProductData(processedData)
    }

    private fun processDataList(productList: List<Product>): List<Product> {
        return productList.map {
            processData(it)
        }
    }

    private fun processData(product: Product): Product {
        //todo: asyc save
        val localImages = getLocalImages(product)
        if (isImageSaved(product) && localImages != null) {
            return product.copy(
                localImage = localImages
            )
        } else {
            val saveImage = saveImages(product)
            if (saveImage != null)
                return product.copy(
                    localImage = saveImage
                )

            return product
        }
    }

    private fun getLocalImages(product: Product): ImageData? {
        val originalImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, ORIGINAL_SIZE_TYPE)
        val smallImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, SMALL_SIZE_TYPE)

        val originalImagePath = productsRepository.getImage(originalImageFilename)
        val smallImagePath = productsRepository.getImage(smallImageFilename)
        if (originalImagePath is ResponseData.Success && smallImagePath is ResponseData.Success) {
            return ImageData(
                originalImagePath.data,
                smallImagePath.data
            )
        }
        return null
    }

    private fun isImageSaved(product: Product): Boolean {
        val originalImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, ORIGINAL_SIZE_TYPE)
        val smallImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, SMALL_SIZE_TYPE)

        return isImageSaved(originalImageFilename) && isImageSaved(smallImageFilename)
    }

    private fun saveImages(product: Product): ImageData? {
        val originalImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, ORIGINAL_SIZE_TYPE)
        val smallImageFilename = IMAGE_IMAGE_FORMAT.format(product.productId, SMALL_SIZE_TYPE)

        val localImages = getLocalImages(product)
        if (localImages != null)
            return localImages

        val imageResponse = productsRepository.downloadImage(product.imagePath)
        if (imageResponse is ResponseData.Fail || imageResponse !is ResponseData.Success)
            return null
        val bitmap = BitmapFactory.decodeStream(imageResponse.data)
        val originalImagePath = productsRepository.storeImage(
            originalImageFilename,
            bitmap
        )
        if (originalImagePath !is ResponseData.Success)
            return null

        val createScaledBitmap = Bitmap.createScaledBitmap(bitmap, 661, 600, false)
        val smallImagePath = productsRepository.storeImage(
            smallImageFilename,
            createScaledBitmap
        )
        if (smallImagePath !is ResponseData.Success)
            return null
        return ImageData(
            originalImagePath.data,
            smallImagePath.data
        )
    }

    private fun isImageSaved(filename: String): Boolean {
        val imageSaved = productsRepository.isImageSaved(filename)
        if (imageSaved is ResponseData.Success)
            return imageSaved.data
        return false
    }
}