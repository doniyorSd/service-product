package com.example.sero_service_admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.ProductAdapter
import com.example.sero_service_admin.databinding.FragmentProductBinding
import com.example.sero_service_admin.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProductFragment : Fragment() {

    lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var adapter:ProductAdapter
    lateinit var list:ArrayList<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        val binding = FragmentProductBinding.bind(view)
        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        myRef = database.getReference("warehouseProduct")

        myRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (child in snapshot.children) {
                    list.add(child.getValue(Product::class.java)!!)
                }
                list.reverse()

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        adapter = ProductAdapter(list, object : ProductAdapter.SellClicks {
            override fun rootClick(product: Product, position: Int) {

            }
        })

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), RecyclerView.VERTICAL
        )
        binding.rv.addItemDecoration(dividerItemDecoration)
        binding.rv.adapter = adapter


        return view
    }
}