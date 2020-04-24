package com.turkcell.tech_assignment.bekarys.di.productdetails

import androidx.lifecycle.ViewModel
import com.turkcell.tech_assignment.bekarys.di.scopes.ActivityScope
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController
import com.turkcell.tech_assignment.bekarys.features.productdetails.domain.ProductDetailsUseCase
import com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.viewmodel.ProductDetailsViewModel
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsInteractor
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named

@Module
class ProductDetailsModule {

    @ActivityScope
    @Provides
    fun provideProductsUseCase(
        productsInteractor: ProductsInteractor
    ): ProductDetailsUseCase = productsInteractor

    @ActivityScope
    @Provides
    fun providesProductDetailsViewModel(
        productUseCase: ProductDetailsUseCase,
        appScheduler: AppScheduler,
        viewModelMap: ViewModelController
    ): ProductDetailsViewModel {
        val mainViewModel = ProductDetailsViewModel(
            productUseCase,
            appScheduler,
            viewModelMap
        )
        viewModelMap.addViewModel(ProductDetailsViewModel::class.java, mainViewModel)

        return mainViewModel
    }
}