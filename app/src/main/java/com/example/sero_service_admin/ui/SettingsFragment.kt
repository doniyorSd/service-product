package com.example.sero_service_admin.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sero_service_admin.R
import com.example.sero_service_admin.databinding.DialogNumberBinding
import com.example.sero_service_admin.databinding.DialogPasswordBinding
import com.example.sero_service_admin.databinding.DialogUsernameBinding
import com.example.sero_service_admin.databinding.FragmentSettingsBinding
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SettingsFragment : Fragment() {

    private lateinit var refUser:DatabaseReference
    var currentUser :User ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val binding = FragmentSettingsBinding.bind(view)

        refUser = FirebaseDatabase.getInstance().getReference("users")

        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        binding.btnChangePassword.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = layoutInflater.inflate(R.layout.dialog_password,binding.root,false)
            dialog.setView(dialogView)
            dialog.show()

            val bind = DialogPasswordBinding.bind(dialogView)

            bind.btnSve.setOnClickListener {
                val oldPassword = bind.etOldPassword.text.toString()
                if (oldPassword == currentUser?.password){
                    val newPassword = bind.etNewPassword.text.toString()
                    val newRepeatPassword = bind.etNewRepeatPassword.text.toString()
                    if (newPassword.isNotBlank() && newRepeatPassword.isNotBlank()){
                        if (newPassword == newRepeatPassword){
                            refUser.child(currentUser?.id!!).child("password").setValue(newPassword)
                            dialog.dismiss()
                            Toast.makeText(requireContext(), "O'zgartirildi", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.empity), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "eski parol xato", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivEditNumber.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = inflater.inflate(R.layout.dialog_number,binding.root,false)
            dialog.setView(dialogView)
            val bind = DialogNumberBinding.bind(dialogView)
            dialog.show()

            bind.etNumber.setText(currentUser?.phoneNumber ?: "Yo'q")

            bind.btnSve.setOnClickListener {
                val number = bind.etNumber.text.toString()
                if (number.isNotBlank()){
                    refUser.child(currentUser?.id!!).child("phoneNumber").setValue(number)
                    binding.tvNumber.text = number
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "O'zgartirildi", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), getString(R.string.empity), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ivEditUsername.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = inflater.inflate(R.layout.dialog_username,binding.root,false)
            dialog.setView(dialogView)
            val bind = DialogUsernameBinding.bind(dialogView)
            dialog.show()

            bind.etUsername.setText(currentUser?.name ?: "")

            bind.btnSve.setOnClickListener {
                val newUsername = bind.etUsername.text.toString()
                if (newUsername.isNotBlank()){
                    refUser.child(currentUser?.id!!).child("name").setValue(newUsername)
                    binding.tvUsername.text = newUsername
                    dialog.dismiss()
                }else{
                    Toast.makeText(requireContext(), getString(R.string.empity), Toast.LENGTH_SHORT).show()
                }
            }
        }


        refUser.child(id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                binding.tvNumber.text = currentUser?.phoneNumber ?: "Yo'q"
                binding.tvUsername.text = currentUser?.name ?: "Yo'q"
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })

        return view
    }
}