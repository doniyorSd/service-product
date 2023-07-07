package com.example.sero_service_admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.ItemDialogProductBinding
import com.example.sero_service_admin.model.Product

class DialogProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<DialogProductAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(product: Product, position: Int) {
            val bind = ItemDialogProductBinding.bind(itemView)

            Glide.with(itemView).load(product.productImg).into(bind.ivProduct)
            bind.apply {
                tvProductName.text = product.name
                tvCount.text = "${product.productCount} ta"
                tvSum.text = "${product.price * product.productCount!!} So'm"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialog_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(productList[position], position)
    }

    override fun getItemCount(): Int = productList.size
}