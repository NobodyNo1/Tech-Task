package com.turkcell.tech_assignment.bekarys.features.productlist.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.tech_assignment.bekarys.databinding.ItemProductCellBinding
import com.turkcell.tech_assignment.bekarys.features.productlist.domain.model.Product

class ProductsAdapter(
    private val onItemClick: (product: Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    private val listItems: MutableList<Product> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemProductCellBinding.inflate(inflater, parent, false)

        return ProductViewHolder(
            viewBinding,
            onItemClick
        )
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(listItems[position])
    }

    fun updateData(dataList: List<Product>) {
        this.listItems.clear()
        this.listItems.addAll(dataList)
        notifyDataSetChanged()
    }
}
