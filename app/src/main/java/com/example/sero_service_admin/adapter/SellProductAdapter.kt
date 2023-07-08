package com.example.sero_service_admin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.ItemSellProductBinding
import com.example.sero_service_admin.model.SellProduct
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.model.UserEnum

class SellProductAdapter(
    val companyListener: CompanyListener,
    var listProduct: ArrayList<SellProduct>,
    val user: User
) :
    RecyclerView.Adapter<SellProductAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(sellProduct: SellProduct, position: Int) {
            val bind = ItemSellProductBinding.bind(itemView)

            Log.d("AAA", "onBind: ${user.position == UserEnum.USER}")
            Log.d("AAA", "onBind: ${user.position} ${UserEnum.USER}")
            Log.d("AAA", ((sellProduct.debt ?: 0) < 0).toString())

            if (user.position == UserEnum.USER || (sellProduct.debt ?: 0) <= 0)
                bind.btnEdit.visibility = View.GONE
            else
                bind.btnEdit.visibility = View.VISIBLE

            bind.apply {
                if (sellProduct.dateAndTime != null) {
                    tvDate.text = sellProduct.dateAndTime ?: ""
                    tvCompanyName.text = sellProduct.toCompany ?: ""
                    tvDebt.text = "Qarz: ${sellProduct.debt ?: ""} so'm"
                    tvSum.text = "Umumiy summa: ${sellProduct.paySum ?: ""}"
                }
            }
            bind.root.setOnClickListener {
                companyListener.clickClick(sellProduct, position)
            }
            bind.btnEdit.setOnClickListener {
                companyListener.clickEdit(sellProduct, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sell_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(listProduct[position], position)
    }

    override fun getItemCount(): Int = listProduct.size

    interface CompanyListener {
        fun clickClick(sellProduct: SellProduct, position: Int)
        fun clickEdit(sellProduct: SellProduct, position: Int)
    }
}