package com.turkcell.tech_assignment.bekarys.features.productlist.data

import android.graphics.Bitmap
import com.turkcell.tech_assignment.bekarys.features.common.database.AppDatabase
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.NoStoredData
import com.turkcell.tech_assignment.bekarys.features.common.local.LocalDirectories
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local.ProductEntry
import com.turkcell.tech_assignment.bekarys.features.productlist.data.model.local.ProductsDao
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ProductLocalDataStorage(
    private val localDirectories: LocalDirectories,
    private val productsDao: ProductsDao
) {

    @Synchronized
    fun getProducts(): ResponseData<List<ProductEntry>> {
        val products = productsDao.getProducts()
        if (products.isEmpty())
            return ResponseData.Fail(NoStoredData("products table is empty"))
        return ResponseData.Success(
            products
        )
    }

    fun saveImage(filename: String, bitmap: Bitmap): ResponseData<String> {
        val f = File(localDirectories.cacheDirectory, filename)

        return try {
            val out = FileOutputStream(
                f
            )
            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                out
            )
            out.flush()
            out.close()
            return ResponseData.Success(f.path)
        } catch (e: FileNotFoundException) {
            ResponseData.Fail(IllegalStateException(e))
        } catch (e: IOException) {
            ResponseData.Fail(IllegalStateException(e))
        }
    }

    fun isImageSaved(filename: String): ResponseData<Boolean> {
        val f = File(localDirectories.cacheDirectory, filename)
        return ResponseData.Success(f.exists())
    }

    fun getImage(filename: String): ResponseData<String> {
        val f = File(localDirectories.cacheDirectory, filename)
        if (f.exists()) {
            return ResponseData.Success(f.path)
        }
        return ResponseData.Fail(NoStoredData("image not saved"))
    }

    fun updateProducts(productList: List<Product>) {
        val savedProducts = getProducts()
        val productsToSave = productList.map {
            ProductEntry(
                productId = it.productId,
                price = it.price,
                imagePath = it.imagePath,
                name = it.name,
                localOriginalImagePath = it.localImage.originalPath,
                localSmallImagePath = it.localImage.smallImagePath
            )
        }
        if (savedProducts is ResponseData.Fail) {
            //todo : reasoning
            productsDao.insertAll(productsToSave)
        }
        productsDao.updateData(productsToSave)
    }

    fun getProduct(productId: String): ResponseData<ProductEntry> {
        val products = productsDao.getProduct(productId)
        if (products.isEmpty())
            return ResponseData.Fail(NoStoredData("products table is empty"))
        return ResponseData.Success(
            products.first()
        )
    }

    fun updateProduct(product: Product) {
        val productEntry = ProductEntry(
            productId = product.productId,
            price = product.price,
            imagePath = product.imagePath,
            name = product.name,
            localOriginalImagePath = product.localImage.originalPath,
            localSmallImagePath = product.localImage.smallImagePath,
            description = product.description
        )
        productsDao.update(productEntry)
    }
}