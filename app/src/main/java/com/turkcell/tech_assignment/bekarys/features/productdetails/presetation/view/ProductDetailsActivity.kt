package com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.view

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.turkcell.tech_assignment.bekarys.application.ApplicationComponentHolder
import com.turkcell.tech_assignment.bekarys.databinding.ActivityProductDetailsBinding
import com.turkcell.tech_assignment.bekarys.di.productdetails.DaggerProductDetailsComponent
import com.turkcell.tech_assignment.bekarys.features.productdetails.presetation.viewmodel.ProductDetailsViewModel
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product
import javax.inject.Inject


class ProductDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ProductDetailsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDI()
        initUI()
        initViewModel()
        setupObservers()

        productId = intent.getStringExtra("product_id")!!
        viewModel.getProduct(productId, true)
    }

    private fun setupDI() {
        DaggerProductDetailsComponent.builder().appComponent(
            ApplicationComponentHolder.component
        ).build().inject(this)
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.refreshProduct.setOnRefreshListener {
            viewModel.getProduct(productId, false)
            binding.refreshProduct.isRefreshing = true
        }
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)
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

    private fun updateData(product: Product) {
        Glide.with(this)
            .load(product.localImage.originalPath)
            .into(binding.productImage)
        binding.productName.text = product.name
        binding.productPrice.text = "${product.price}"
        binding.productDetails.text = product.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
