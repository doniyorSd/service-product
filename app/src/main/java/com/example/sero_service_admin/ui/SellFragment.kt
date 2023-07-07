package com.example.sero_service_admin.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.DialogProductAdapter
import com.example.sero_service_admin.adapter.SellProductAdapter
import com.example.sero_service_admin.databinding.DialogProductBinding
import com.example.sero_service_admin.databinding.FragmentSellBinding
import com.example.sero_service_admin.model.SellProduct
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SellFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_sell, container, false)





        return view
    }
}







