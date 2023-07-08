package com.example.sero_service_admin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sero_service_admin.ui.AddNewProductFragment
import com.example.sero_service_admin.ui.AddOldFragment
import com.example.sero_service_admin.ui.AddProductWarehouseFragment
import com.example.sero_service_admin.ui.ProductFragment

class ProductViewPagerAdapter(fragmentActivity: FragmentActivity,val listFragment:List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment = listFragment[position]
        // if (position == 0) AddProductWarehouseFragment() else if (position == 1) ProductFragment() else AddOldFragment()
}