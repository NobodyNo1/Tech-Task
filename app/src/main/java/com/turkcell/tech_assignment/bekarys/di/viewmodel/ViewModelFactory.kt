package com.turkcell.tech_assignment.bekarys.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController

class ViewModelFactory(
    private val viewModels: ViewModelController
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels.getViewModel(modelClass) as T
}