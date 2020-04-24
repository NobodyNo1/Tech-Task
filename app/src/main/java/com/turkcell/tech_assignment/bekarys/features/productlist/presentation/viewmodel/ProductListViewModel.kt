package com.turkcell.tech_assignment.bekarys.features.productlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsUseCase
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProductListViewModel(
    private val productsUseCase: ProductsUseCase,
    private val appScheduler: AppScheduler
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val productListLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    private val error = MutableLiveData<Throwable>()
    private val productLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getProductLiveData(): LiveData<List<Product>> = productListLiveData
    fun getErrorLiveData(): LiveData<Throwable> = error
    fun getProductLoadingLiveData(): LiveData<Boolean> = productLoading

    init {
        getProducts(true)
    }

    fun getProducts(fromCache: Boolean) {
        if (productLoading.value == true)
            return

        productLoading.value = true
        val disposableProductRequest = Observable.fromCallable {
            productsUseCase.getProducts(fromCache)
        }.subscribeOn(appScheduler.getIOScheduler())
            .observeOn(appScheduler.getUIScheduler())
            .subscribe(
                {
                    productLoading.postValue(false)
                    if (it is ResponseData.Success)
                        productListLiveData.postValue(it.data)
                    else if (it is ResponseData.Fail) {
                        error.postValue(it.exception)
                    }
                }, {
                    error.postValue(it)
                    productLoading.postValue(false)
                }
            )
        compositeDisposable.add(disposableProductRequest)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}