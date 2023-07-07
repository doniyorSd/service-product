package com.example.sero_service_admin.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.DialogProductAdapter
import com.example.sero_service_admin.adapter.SellProductAdapter
import com.example.sero_service_admin.databinding.DialogAddCompanyBinding
import com.example.sero_service_admin.databinding.DialogProductBinding
import com.example.sero_service_admin.databinding.FragmentHomeBinding
import com.example.sero_service_admin.model.SellProduct
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var database: FirebaseDatabase
    lateinit var refSellProduct: DatabaseReference
    lateinit var refUser: DatabaseReference
    lateinit var adapter: SellProductAdapter
    lateinit var list: ArrayList<SellProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)


        database = FirebaseDatabase.getInstance()
        refSellProduct = database.getReference("sellProduct")
        refUser = database.getReference("users")
        list = ArrayList()


        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        refUser.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.tvTopBar.text = user?.name
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })

        refSellProduct.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                snapshot.children.forEach { sell ->
                    list.add(sell.getValue(SellProduct::class.java)!!)
                }
                list.reverse()

                val listDate = ArrayList<SellProduct>()

                val currentDate = LocalDate.now()

                list.forEach {
                    if (it.dateAndTime!= null){
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                        val dateTime = LocalDateTime.parse(it.dateAndTime, formatter)

                        if (currentDate.isEqual(ChronoLocalDate.from(dateTime))){
                            listDate.add(it)
                        }
                    }

                }
                val format: NumberFormat = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("UZS")

                val ss = listDate.sumOf { it.paySum!! }
                val form = format.format(ss).substring(3)
                binding.tvSum.text = "Bir kunlik tushum $form"

                adapter.notifyDataSetChanged()
                Log.d("AAA", "sell: $list")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.rvProduct.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView =
                layoutInflater.inflate(R.layout.dialog_add_company, binding.root, false)
            dialog.setView(dialogView)
            dialog.show()
            val bind = DialogAddCompanyBinding.bind(dialogView)

            bind.addCompanyButton.setOnClickListener {
                val companyName = bind.inEtCompanyName.text.toString()
                if (companyName.isNotBlank()) {
                    val key = refSellProduct.push().key
                    val sellProduct = SellProduct(id = key, toCompany = companyName)
                    refSellProduct.child(key!!).setValue(sellProduct)

                    findNavController().navigate(R.id.sellMainFragment, Bundle().apply {
                        putSerializable("sell", sellProduct)
                    })
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.empity), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }



        adapter = SellProductAdapter(object : SellProductAdapter.CompanyListener {
            override fun clickClick(sellProduct: SellProduct, position: Int) {
                if (sellProduct.productList?.isNotEmpty()!!) {
                    val dialog = AlertDialog.Builder(requireContext()).create()
                    val dialogView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_product, binding.root, false)
                    dialog.setView(dialogView)
                    dialog.show()
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val bind = DialogProductBinding.bind(dialogView)

                    bind.tvDebt.text = "Qarz: ${sellProduct.debt ?: ""} so'm"
                    bind.tvSum.text = "Umumiy summa ${sellProduct.paySum ?: ""} so'm"
                    if (sellProduct.debt != null)
                        bind.tvPaid.text =
                            "To'langan summa ${sellProduct.paySum!! - sellProduct.debt!!} so'm"

                    val dialogAdapter =
                        DialogProductAdapter(sellProduct.productList!!)

                    bind.rvProduct.adapter = dialogAdapter

                } else {
                    Toast.makeText(requireContext(), "Malumot yetarli emas", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun editClick(sellProduct: SellProduct, position: Int) {
                findNavController().navigate(R.id.sellMainFragment, bundleOf("sell" to sellProduct))
            }
        }, list)

        binding.rvProduct.adapter = adapter

        return view
    }
}

