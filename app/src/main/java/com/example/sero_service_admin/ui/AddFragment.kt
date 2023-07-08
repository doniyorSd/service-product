package com.example.sero_service_admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.ProductViewPagerAdapter
import com.example.sero_service_admin.databinding.FragmentAddBinding
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.model.UserEnum
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddFragment : Fragment() {

    lateinit var binding: FragmentAddBinding
    lateinit var adapter: ProductViewPagerAdapter
    var user :User?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        binding = FragmentAddBinding.bind(view)



        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("users")

        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        refUser.child(id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)


                if (user!= null){
                    adapter = if (user?.position == UserEnum.USER){
                        ProductViewPagerAdapter(requireActivity(), listOf(AddProductWarehouseFragment()))
                    }else{
                        ProductViewPagerAdapter(requireActivity(), listOf(AddProductWarehouseFragment(),ProductFragment(),AddOldFragment()))
                    }
                }

                binding.viewPager.adapter = adapter




                TabLayoutMediator(
                    binding.tabLayout, binding.viewPager
                ) { tab, position ->
                    tab.text =if (position == 0) "Ombor" else if (position == 1) "Mahsulot tarixi" else "Eski Mahsulot qoshish"

                }.attach()
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })




        return view
    }


}