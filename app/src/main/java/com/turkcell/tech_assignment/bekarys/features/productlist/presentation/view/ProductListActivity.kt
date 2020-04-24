package com.turkcell.tech_assignment.bekarys.features.productlist.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.turkcell.tech_assignment.bekarys.application.ApplicationComponentHolder
import com.turkcell.tech_assignment.bekarys.features.productlist.presentation.viewmodel.ProductListViewModel
import com.turkcell.tech_assignment.bekarys.features.productlist.presentation.view.adapter.ProductsAdapter
import com.turkcell.tech_assignment.bekarys.databinding.ActivityProductListBinding
import com.turkcell.tech_assignment.bekarys.di.productlist.DaggerProductListComponent
import com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.view.ProductDetailsActivity
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import javax.inject.Inject

class ProductListActivity : AppCompatActivity() {

    private val productsAdapter =
        ProductsAdapter {
            openProductDetailScreen(it)
        }

    @Inject
    lateinit var viewModel: ProductListViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDI()
        initUI()
        initViewModel()
        setupObservers()
    }

    private fun initUI() {
        setupProductsView()
        binding.refreshProduct.setOnRefreshListener {
            viewModel.getProducts(false)
            binding.refreshProduct.isRefreshing = true
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductListViewModel::class.java)
    }

    private fun setupProductsView() {
        binding.productsRv.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.productsRv.adapter = productsAdapter
    }

    private fun setupDI() {
        DaggerProductListComponent.builder().appComponent(
            ApplicationComponentHolder.component
        ).build().inject(this)
    }

    private fun setupObservers() {
        viewModel.getProductLiveData().observe({ lifecycle }, { data ->
            if (data == null) return@observe
            updateData(data)
        })
        viewModel.getProductLoadingLiveData().observe({ lifecycle }, { isLoading ->
            if (isLoading == null) return@observe
            binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.refreshProduct.isEnabled = !isLoading
            if (!isLoading)
                binding.refreshProduct.isRefreshing = false
        })
        viewModel.getErrorLiveData().observe({ lifecycle }, { error ->
            if (error == null) return@observe
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateData(dataList: List<Product>) {
        productsAdapter.updateData(dataList)
    }

    private fun openProductDetailScreen(it: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("product_id", it.productId)
        startActivity(intent)
    }
}
