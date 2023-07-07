package com.example.sero_service_admin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sero_service_admin.ui.AddNewProductFragment
import com.example.sero_service_admin.ui.AddOldFragment

class ProductViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        if (position == 0) AddNewProductFragment() else AddOldFragment()
}