package com.example.sero_service_admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.FragmentRegistrationBinding
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        binding = FragmentRegistrationBinding.bind(view)

        database = Firebase.database
        myRef = database.getReference("users")

        MySharedPreference.init(requireContext())

        val id = MySharedPreference.id


        if (id != "no") {
            findNavController().navigate(R.id.homeFragment)
        }



        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isHave = false
                    for (dataSnapshot in snapshot.children) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (user?.login == name && user.password == password) {
                            isHave = true
                            MySharedPreference.id = user.id
                            break
                        }
                    }
                    if (isHave) {
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Login yoki parol xato",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        return view
    }
}