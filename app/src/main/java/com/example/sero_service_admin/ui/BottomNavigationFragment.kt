package com.example.sero_service_admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {
    lateinit var binding: FragmentBottomNavigationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false)
        binding = FragmentBottomNavigationBinding.bind(view)




        return view
    }
}