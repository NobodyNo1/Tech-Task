package com.turkcell.tech_assignment.bekarys.features.common.viewmodel

import androidx.lifecycle.ViewModel
import com.turkcell.tech_assignment.bekarys.features.productlist.presentation.viewmodel.ProductListViewModel
import java.util.*

class ViewModelController {

    private val viewModelMap: WeakHashMap<Class<out ViewModel>, ViewModel> = WeakHashMap()

    fun addViewModel(key: Class<out ViewModel>, value: ViewModel) {
        if (viewModelMap.containsKey(key))
            return
        viewModelMap[key] = value
    }

    fun getViewModel(key: Class<out ViewModel>) = viewModelMap[key]

    fun removeViewModel(key: Class<out ViewModel>) {
        viewModelMap.remove(key)
    }
}