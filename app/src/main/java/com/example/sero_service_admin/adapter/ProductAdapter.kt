package com.example.sero_service_admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.ItemProductBinding
import com.example.sero_service_admin.model.Product

class ProductAdapter(private val listProduct:List<Product>,val sellClicks: SellClicks) :RecyclerView.Adapter<ProductAdapter.ProductHolder>(){
    inner class ProductHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun onBind(product: Product,position: Int){
            val bind = ItemProductBinding.bind(itemView)
            Glide.with(itemView).load(product.productImg).into(bind.ivProduct)

            if (product.sellCount == null){
                bind.productCount.text = "${product.productCount}"
            }else{
                bind.productCount.text = "${product.sellCount ?: ""} / ${product.productCount}"
            }

            bind.productName.text = product.name

            bind.root.setOnClickListener {
                sellClicks.rootClick(product,position)
            }
            bind.btnEdit.setOnClickListener {
                sellClicks.editProduct(product, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false))
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.onBind(listProduct[position],position)
    }

    interface SellClicks{
        fun editProduct(product: Product,position: Int)
        fun rootClick(product: Product,position: Int)
    }

}