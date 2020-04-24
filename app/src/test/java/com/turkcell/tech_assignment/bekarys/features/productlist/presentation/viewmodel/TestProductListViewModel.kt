package com.turkcell.tech_assignment.bekarys.features.productlist.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.turkcell.tech_assignment.bekarys.features.common.threading.TestAppScheduler
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsUseCase
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.lang.Thread.currentThread
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class TestProductListViewModel {

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private var appScheduler = TestAppScheduler()

    @Test
    fun `test initial data when success result`() {
        val dataObserver = Mockito.mock(
            Observer::class.java
        ) as Observer<List<Product>>

        val errorObserver = Mockito.mock(
            Observer::class.java
        ) as Observer<Throwable>

        val loadingObserver = Mockito.mock(
            Observer::class.java
        ) as Observer<Boolean>

        val fromCache = true
        val expected = ResponseData.Success<List<Product>>(
            emptyList()
        )

        val productInteractor = Mockito.mock(ProductsUseCase::class.java)
        Mockito.`when`(productInteractor.getProducts(fromCache)).thenReturn(expected)

        val productListViewModel =
            ProductListViewModel(productInteractor, appScheduler = appScheduler)

        productListViewModel.getProductLiveData().observeForever(dataObserver)
        productListViewModel.getProductLoadingLiveData().observeForever(loadingObserver)
        productListViewModel.getErrorLiveData().observeForever(errorObserver)

        Mockito.verify<Observer<List<Product>>>(
            dataObserver,
            Mockito.atLeastOnce()
        ).onChanged(expected.data)

        Mockito.verify<Observer<Boolean>>(
            loadingObserver,
            Mockito.atLeastOnce()
        ).onChanged(false)

        Mockito.verify<Observer<Throwable>>(
            errorObserver,
            Mockito.never()
        ).onChanged(IllegalArgumentException())
    }

    @Test
    fun `test initial data when fail result`() {
        val observer = Mockito.mock(
            Observer::class.java
        ) as Observer<List<Product>>
        val errorObserver = Mockito.mock(
            Observer::class.java
        ) as Observer<Throwable>

        val loadingObserver = Mockito.mock(
            Observer::class.java
        ) as Observer<Boolean>


        val fromCache = true
        val exception = IllegalArgumentException()
        val expected = ResponseData.Fail<List<Product>>(
            exception
        )

        val productInteractor = Mockito.mock(ProductsUseCase::class.java)
        Mockito.`when`(productInteractor.getProducts(fromCache)).thenReturn(expected)

        val productListViewModel =
            ProductListViewModel(productInteractor, appScheduler = appScheduler)


        productListViewModel.getErrorLiveData().observeForever(errorObserver)
        productListViewModel.getProductLiveData().observeForever(observer)
        productListViewModel.getProductLoadingLiveData().observeForever(loadingObserver)

        Mockito.verify<Observer<List<Product>>>(
            observer,
            Mockito.never()
        ).onChanged(
            emptyList()
        )
        Mockito.verify<Observer<Throwable>>(
            errorObserver,
            Mockito.atLeastOnce()
        ).onChanged(
            exception
        )

        Mockito.verify<Observer<Boolean>>(
            loadingObserver,
            Mockito.atLeastOnce()
        ).onChanged(false)
    }
}