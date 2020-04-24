package com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController
import com.turkcell.tech_assignment.bekarys.features.productdetails.domain.ProductDetailsUseCase
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import com.turkcell.tech_assignment.bekarys.utils.ResponseData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ProductDetailsViewModel(
    private val productDetailUseCase: ProductDetailsUseCase,
    private val appScheduler: AppScheduler,
    private val viewModelController: ViewModelController
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val productDetailLiveData: MutableLiveData<Product> = MutableLiveData()
    private val error = MutableLiveData<Throwable>()
    private val productLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getProductLiveData(): LiveData<Product> = productDetailLiveData
    fun getErrorLiveData(): LiveData<Throwable> = error
    fun getProductLoadingLiveData(): LiveData<Boolean> = productLoading

    fun getProduct(productId: String, fromCache: Boolean) {
        if (productLoading.value == true)
            return

        productLoading.value = true
        val disposableProductRequest = Observable.fromCallable {
            productDetailUseCase.getProductDetails(productId, fromCache)
        }.subscribeOn(appScheduler.getIOScheduler())
            .observeOn(appScheduler.getUIScheduler())
            .subscribe(
                {
                    productLoading.postValue(false)
                    if (it is ResponseData.Success)
                        productDetailLiveData.postValue(it.data)
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
        viewModelController.removeViewModel(this::class.java)
    }
}