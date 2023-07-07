package com.example.sero_service_admin.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.ProductAdapter
import com.example.sero_service_admin.databinding.DialogPayBinding
import com.example.sero_service_admin.databinding.DialogSellEtBinding
import com.example.sero_service_admin.databinding.FragmentSellMainBinding
import com.example.sero_service_admin.model.Product
import com.example.sero_service_admin.model.SellProduct
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.example.sero_service_admin.utils.MySharedPreference.id
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SellMainFragment : Fragment() {

    lateinit var adapter: ProductAdapter
    lateinit var refProduct: DatabaseReference
    lateinit var refSellProduct: DatabaseReference
    lateinit var refUser: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var listProduct: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sell_main, container, false)
        val binding = FragmentSellMainBinding.bind(view)

        MySharedPreference.init(requireContext())

        val id = MySharedPreference.id
        database = FirebaseDatabase.getInstance()

        refProduct = database.getReference("products")
        refSellProduct = database.getReference("sellProduct")
        refUser = database.getReference("users")

        var currentUser = User()

        refUser.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val sellProduct = arguments?.getSerializable("sell") as SellProduct

        listProduct = ArrayList()
        listProduct.addAll(sellProduct.productList?: emptyList())

        var sum = 0

        refProduct.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listProduct.clear()

                snapshot.children.forEach {
                    listProduct.add(
                        it.getValue(Product::class.java)!!
                    )
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        binding.tvCompanyName.text = sellProduct.toCompany


        adapter = ProductAdapter(listProduct, object : ProductAdapter.SellClicks {
            override fun editProduct(product: Product, position: Int) {

            }

            override fun rootClick(product: Product, position: Int) {
                val dialog = AlertDialog.Builder(requireContext()).create()
                val dialogView =
                    layoutInflater.inflate(R.layout.dialog_sell_et, binding.root, false)
                val bind = DialogSellEtBinding.bind(dialogView)
                dialog.setView(dialogView)
                dialog.show()
                bind.apply {
                    tvCount.text = product.productCount.toString() + " ta qoldi"
                    tvName.text = product.name
                    etCount.addTextChangedListener { etCount ->
                        if (etCount?.length != 0 && bind.inEtPrice.text!!.toString().isNotBlank()) {
                            if (etCount.toString().toInt() <= product.productCount!!) {

                                val price = bind.inEtPrice.text.toString().toInt()
                                bind.tvSum.text = "${price * etCount.toString().toInt()} so'm"
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Mahsulot yetarli emas",
                                    Toast.LENGTH_SHORT
                                ).show()
                                bind.tvSum.text = "0 So'm"
                            }
                        } else {
                            bind.tvSum.text = "0 So'm"
                        }
                    }
                    inEtPrice.addTextChangedListener { etPrice ->
                        if (etPrice?.length != 0 && bind.etCount.text!!.toString().isNotBlank()) {
                            val count = bind.etCount.text.toString().toInt()

                            if (count <= product.productCount!!) {
                                bind.tvSum.text = "${count * etPrice.toString().toInt()} so'm"
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Mahsulot yetarli emas",
                                    Toast.LENGTH_SHORT
                                ).show()
                                bind.tvSum.text = "0 So'm"
                            }
                        } else {
                            bind.tvSum.text = "0 So'm"
                        }
                    }
                    btnAdd.setOnClickListener {
                        val count = etCount.text.toString()
                        val price = inEtPrice.text.toString()
                        if (count.isNotBlank() && price.isNotBlank()) {
                            if (count.toInt() <= product.productCount!!) {
                                sellProduct.productList?.add(product)

                                sum += count.toInt() * price.toInt()
                                binding.tvSum.text = "Umumiy summa $sum So'm"
                                product.price = price.toInt()
                                product.sellCount = count.toInt()
                                adapter.notifyItemChanged(position)
                                Toast.makeText(requireContext(), "Qo'shildi", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(requireContext(), "yetarli emas", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getText(R.string.empity),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })


        binding.btnFinish.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = inflater.inflate(R.layout.dialog_pay, binding.root, false)
            val bind = DialogPayBinding.bind(dialogView)
            dialog.setView(dialogView)
            dialog.show()

            bind.tvSum.text = binding.tvSum.text

            bind.fillPay.setOnClickListener {
                bind.etPay.setText(sum.toString())
            }

            bind.etPay.addTextChangedListener { pay ->
                if (pay!!.isNotBlank()) {
                    val debt = sum - pay.toString().toInt()
                    bind.tvDebt.text = "Qarz: $debt so'm"
                } else {
                    bind.tvSum.text = "Qarz: $sum so'm"
                }
            }


            bind.btnFinish.setOnClickListener {
                if (bind.etPay.text!!.isNotBlank()) {
                    val current = LocalDateTime.now()

                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    val formatted = current.format(formatter)


                    sellProduct.apply {
                        debt = sum - bind.etPay.text.toString().toInt()
                        dateAndTime = formatted
                        soldUser = currentUser
                        paySum = sum
                    }
                    sellProduct.productList

                    // Admin,User
                    // User -> show Product , show Debt Fragment

                    refSellProduct.child(sellProduct.id!!).setValue(sellProduct)

                    refProduct.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val newListForCalculate = ArrayList<Product>()

                            snapshot.children.forEach {
                                newListForCalculate.add(
                                    it.getValue(Product::class.java)!!
                                )
                            }
                            Log.d("AAA", "onDataChange: $newListForCalculate")
                            Log.d("AAA", "onDataChange: ${sellProduct.productList}")


                            newListForCalculate.forEach { product ->
                                Log.d(
                                    "AAA",
                                    "productsss :$product ${
                                        sellProduct.productList!!.contains(product)
                                    }"
                                )

                                sellProduct.productList!!.forEach { sellPr ->
                                    if (sellPr.id == product.id) {
                                        val searchProduct =
                                            listProduct.find { sProduct -> sProduct.id == product.id }

                                        product.productCount =
                                            product.productCount!! - searchProduct?.sellCount!!
                                        refProduct.child(product.id!!).setValue(product)
                                    }
                                }

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                    dialog.dismiss()

                    findNavController().popBackStack()
                }else{
                    Toast.makeText(requireContext(), getText(R.string.empity), Toast.LENGTH_SHORT).show()
                }
            }
        }
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), RecyclerView.VERTICAL
        )
        binding.rvSellProduct.addItemDecoration(dividerItemDecoration)
        binding.rvSellProduct.adapter = adapter

        return view
    }
}