package com.turkcell.tech_assignment.bekarys.di.productlist

import com.turkcell.tech_assignment.bekarys.di.scopes.ActivityScope
import com.turkcell.tech_assignment.bekarys.features.common.database.AppDatabase
import com.turkcell.tech_assignment.bekarys.features.common.local.LocalDirectories
import com.turkcell.tech_assignment.bekarys.features.common.network.NetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.network.RequestManager
import com.turkcell.tech_assignment.bekarys.features.productlist.data.DefaultProductRepository
import com.turkcell.tech_assignment.bekarys.features.productlist.data.ProductLocalDataStorage
import com.turkcell.tech_assignment.bekarys.features.productlist.data.ProductNetworkDataStorage
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsInteractor
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.ProductsRepository
import dagger.Module
import dagger.Provides

@Module
class ProductDataModule {

    @ActivityScope
    @Provides
    fun provideProductsRepository(
        requestManager: RequestManager,
        localDirectories: LocalDirectories,
        appDatabase: AppDatabase
    ): ProductsRepository =
        DefaultProductRepository(
            ProductLocalDataStorage(localDirectories, appDatabase.products()),
            ProductNetworkDataStorage(requestManager)
        )

    @ActivityScope
    @Provides
    fun provideProductsInteractor(
        productsRepository: ProductsRepository,
        networkStateManager: NetworkStateManager
    ): ProductsInteractor = ProductsInteractor(
        networkStateManager,
        productsRepository
    )
}