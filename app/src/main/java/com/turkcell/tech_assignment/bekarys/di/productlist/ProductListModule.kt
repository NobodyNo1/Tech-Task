package com.turkcell.tech_assignment.bekarys.di.productlist

import androidx.lifecycle.ViewModel
import com.turkcell.tech_assignment.bekarys.di.scopes.ActivityScope
import com.turkcell.tech_assignment.bekarys.features.common.database.AppDatabase
import com.turkcell.tech_assignment.bekarys.features.common.local.LocalDirectories
import com.turkcell.tech_assignment.bekarys.features.common.network.NetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.network.RequestManager
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController
import com.turkcell.tech_assignment.bekarys.features.productlist.data.DefaultProductRepository
import com.turkcell.tech_assignment.bekarys.features.productlist.data.ProductLocalDataStorage
import com.turkcell.tech_assignment.bekarys.features.productlist.data.ProductNetworkDataStorage
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsInteractor
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsRepository
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsUseCase
import com.turkcell.tech_assignment.bekarys.features.productlist.presentation.viewmodel.ProductListViewModel
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named

@Module
class ProductListModule {


    @ActivityScope
    @Provides
    fun provideProductsUseCase(
        productsInteractor: ProductsInteractor
    ): ProductsUseCase = productsInteractor

    /***
     * I am aware that there solution with ViewModelKey approach
     * but it is not appealing, because it will require that in the application there will be
     * one component
     *
     * this approach is raw, but i am currently experimenting
     */
    @ActivityScope
    @Provides
    fun providesMainViewModel(
        productsUseCase: ProductsUseCase,
        appScheduler: AppScheduler,
        viewModelController: ViewModelController
    ): ProductListViewModel {
        val mainViewModel = ProductListViewModel(
            productsUseCase,
            appScheduler
        )
        viewModelController.addViewModel(ProductListViewModel::class.java, mainViewModel)

        return mainViewModel
    }


}