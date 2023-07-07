package com.example.sero_service_admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.FragmentAddOldBinding
import com.example.sero_service_admin.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddOldFragment : Fragment() {
    lateinit var binding: FragmentAddOldBinding
    lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var listProduct: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_old, container, false)
        binding = FragmentAddOldBinding.bind(view)

        database = FirebaseDatabase.getInstance()

        myRef = database.getReference("products")
        listProduct = ArrayList()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listProduct.clear()

                snapshot.children.forEach {
                    listProduct.add(
                        it.getValue(Product::class.java)!!
                    )
                }

                val newList = listProduct.map {
                    it.name
                }

                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, newList)
                (binding.textFiled.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.addButton.setOnClickListener {
            val name = binding.inTextField.text.toString()
            val count = binding.inEtProductCount.text.toString()

            if (name.isNotBlank() && count.isNotBlank()) {
                val product = listProduct.find {
                    it.name == name
                }
                product?.productCount = product?.productCount!! + count.toInt()
                myRef.child(product.id!!).setValue(product)
                Toast.makeText(requireContext(), "Qo'shildi", Toast.LENGTH_SHORT).show()

                binding.inTextField.text.clear()
                binding.inEtProductCount.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Malumot to'liq emas", Toast.LENGTH_SHORT).show()
            }
        }



        return view
    }
}