package com.turkcell.tech_assignment.bekarys.di.productlist

import com.turkcell.tech_assignment.bekarys.di.app.AppComponent
import com.turkcell.tech_assignment.bekarys.di.scopes.ActivityScope
import com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.view.ProductDetailsActivity
import com.turkcell.tech_assignment.bekarys.features.productlist.presentation.view.ProductListActivity
import dagger.Component


@Component(
    modules = [
        ProductDataModule::class,
        ProductListModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
@ActivityScope
interface ProductListComponent {

    fun inject(activity: ProductListActivity)
}