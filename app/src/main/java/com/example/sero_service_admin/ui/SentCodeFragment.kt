package com.example.sero_service_admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.FragmentSentCodeBinding

class SentCodeFragment : Fragment() {
    lateinit var binding: FragmentSentCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sent_code, container, false)
        binding = FragmentSentCodeBinding.bind(view)


        return view
    }
}