package com.example.sero_service_admin.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.UserAdapter
import com.example.sero_service_admin.databinding.DialogUserBinding
import com.example.sero_service_admin.databinding.FragmentAddUserBinding
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.model.UserEnum
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AddUserFragment : Fragment() {

    lateinit var adapter: UserAdapter
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    lateinit var listUser: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = Firebase.database
        myRef = database.getReference("users")

        listUser = ArrayList()

        val view = inflater.inflate(R.layout.fragment_add_user, container, false)
        val binding = FragmentAddUserBinding.bind(view)

        adapter = UserAdapter(object : UserAdapter.ClickListener {

            override fun onRemoveClick(user: User) {
                myRef.child(user.id!!).removeValue()
            }

            override fun onEditClick(user: User) {
                val dialog = AlertDialog.Builder(requireContext()).create()
                val dialogView = inflater.inflate(R.layout.dialog_user, binding.root, false)
                dialog.setView(dialogView)
                dialog.show()
                val dialogBinding = DialogUserBinding.bind(dialogView)

                dialogBinding.inEtUserName.setText(user.name)
                dialogBinding.inEtUserLogin.setText(user.login)
                dialogBinding.inUserPassword.setText(user.password)

                dialogBinding.addButton.text = "Edit"

                dialogBinding.addButton.setOnClickListener {

                    user.apply {
                        name = dialogBinding.inEtUserName.text.toString()
                        login = dialogBinding.inEtUserLogin.text.toString()
                        login = dialogBinding.inUserPassword.text.toString()
                        phoneNumber = dialogBinding.inUserNumber.text.toString()
                    }

                    myRef.child(user.id!!).setValue(user)
                    Toast.makeText(requireContext(), "Edited", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        })
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), RecyclerView.VERTICAL
        )
        binding.rv.addItemDecoration(dividerItemDecoration)
        binding.rv.adapter = adapter

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for (postSnapshot in snapshot.children) {
                    list.add(postSnapshot.getValue(User::class.java)!!)
                }
                adapter.submitList(list.toMutableList())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("AAA", "onCancelled: ${error.message}")
            }

        })

        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = inflater.inflate(R.layout.dialog_user, binding.root, false)
            dialog.setView(dialogView)
            dialog.show()
            val dialogBinding = DialogUserBinding.bind(dialogView)

            dialogBinding.addButton.setOnClickListener {
                val username = dialogBinding.inEtUserName.text.toString()
                val login = dialogBinding.inEtUserLogin.text.toString()
                val password = dialogBinding.inUserPassword.text.toString()
                val number = dialogBinding.inUserNumber.text.toString()

                val id = myRef.push().key!!

                val user = User(id, login, username, password, UserEnum.USER, number)
                myRef.child(id).setValue(user)

                Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        return view
    }

}