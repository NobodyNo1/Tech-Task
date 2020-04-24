package com.turkcell.tech_assignment.bekarys.features.productlist.domain

import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.ConnectionException
import com.turkcell.tech_assignment.bekarys.features.common.exceptions.customexceptions.EmptyDataException
import com.turkcell.tech_assignment.bekarys.features.common.network.TestNetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.productlist.data.ProductsRepositoryStab
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import org.junit.Assert.assertEquals
import org.junit.Test

class TestProductInteractor {

    val networkStateManager = TestNetworkStateManager()
    val testProductsRepository = ProductsRepositoryStab()

    private val productInteractor = ProductsInteractor(
        networkStateManager = networkStateManager,
        productsRepository = testProductsRepository
    )

    @Test
    fun `test loading from api when no network `() {
        val expected = ResponseData.Fail<List<Product>>(ConnectionException.NoConnection)

        val fromCache = false
        networkStateManager.isNetworkAvailable = false
        val actual = productInteractor.getProducts(fromCache)
        assertEquals(expected, actual)
    }

    @Test
    fun `test loading from cache when there exist saved data `() {
        val imageMock = ""
        val isImageSavedMock = true
        val exceptedData = listOf(Product())
        val expected = ResponseData.Success<List<Product>>(exceptedData)

        val fromCache = true
        networkStateManager.isNetworkAvailable = false
        testProductsRepository.responseForGetProductsFromCache = expected
        testProductsRepository.responseForIsImageSaved = ResponseData.Success(isImageSavedMock)
        testProductsRepository.responseForGetImage = ResponseData.Success(imageMock)

        val actual = productInteractor.getProducts(fromCache)
        assertEquals(expected, actual)
    }

    @Test
    fun `test loading from cache when there are no saved data and no network `() {
        val imageMock = ""
        val isImageSavedMock = true
        val localData = emptyList<Product>()

        val expected = ResponseData.Fail<List<Product>>(ConnectionException.NoConnection)

        val fromCache = false
        networkStateManager.isNetworkAvailable = false

        testProductsRepository.responseForGetProductsFromCache = ResponseData.Success(localData)
        testProductsRepository.responseForGetProductsFromNetwork = expected

        testProductsRepository.responseForIsImageSaved = ResponseData.Success(isImageSavedMock)
        testProductsRepository.responseForGetImage = ResponseData.Success(imageMock)

        val actual = productInteractor.getProducts(fromCache)
        assertEquals(expected, actual)
    }

    @Test
    fun `test loading from cache when there are no saved data `() {
        val imageMock = ""
        val isImageSavedMock = true
        val localData = emptyList<Product>()

        val exceptedData = listOf(Product())
        val expected = ResponseData.Success<List<Product>>(exceptedData)

        val fromCache = false
        networkStateManager.isNetworkAvailable = true

        testProductsRepository.responseForGetProductsFromCache = ResponseData.Success(localData)
        testProductsRepository.responseForGetProductsFromNetwork = expected

        testProductsRepository.responseForIsImageSaved = ResponseData.Success(isImageSavedMock)
        testProductsRepository.responseForGetImage = ResponseData.Success(imageMock)

        val actual = productInteractor.getProducts(fromCache)
        assertEquals(expected, actual)
    }

    @Test
    fun `test loading from cache when there are no saved data and network response is empty `() {
        val imageMock = ""
        val isImageSavedMock = true
        val localData = emptyList<Product>()
        val networkData = emptyList<Product>()

        val exceptedData = listOf(Product())
        val expected =
            ResponseData.Fail<List<Product>>(EmptyDataException("The products are empty"))

        val fromCache = false
        networkStateManager.isNetworkAvailable = true

        testProductsRepository.responseForGetProductsFromCache = ResponseData.Success(localData)
        testProductsRepository.responseForGetProductsFromNetwork = ResponseData.Success(networkData)

        testProductsRepository.responseForIsImageSaved = ResponseData.Success(isImageSavedMock)
        testProductsRepository.responseForGetImage = ResponseData.Success(imageMock)

        val actual = productInteractor.getProducts(fromCache)
        assertEquals(expected.message, (actual as? ResponseData.Fail)?.message)
    }

}