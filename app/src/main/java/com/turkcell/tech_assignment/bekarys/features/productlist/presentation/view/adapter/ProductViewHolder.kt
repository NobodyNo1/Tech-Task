package com.turkcell.tech_assignment.bekarys.features.productlist.presentation.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.turkcell.tech_assignment.bekarys.databinding.ItemProductCellBinding
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product

class ProductViewHolder(
    private val viewBinding: ItemProductCellBinding,
    private val onItemClick: (product: Product) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(product: Product) {
        Glide.with(viewBinding.itemProductImage.context)
            .load(product.localImage.smallImagePath)
            .into(viewBinding.itemProductImage)
        viewBinding.itemProductName.text = product.name
        viewBinding.itemProductPrice.text = "${product.price}"
        viewBinding.root.setOnClickListener {
            onItemClick(product)
        }
    }
}
