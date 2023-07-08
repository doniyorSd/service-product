package com.example.sero_service_admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.MessageAdapter
import com.example.sero_service_admin.databinding.FragmentCHatBinding
import com.example.sero_service_admin.model.Message
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CHatFragment : Fragment() {

    var user: User?= null
    lateinit var adapter:MessageAdapter
    lateinit var list:ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        list = ArrayList()
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_c_hat, container, false)
        val binding = FragmentCHatBinding.bind(view)
        val refChat = FirebaseDatabase.getInstance().getReference("messages")
        val refUser = FirebaseDatabase.getInstance().getReference("users")

        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        refUser.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                adapter = MessageAdapter(list,user?.id!!)
                binding.rv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })
        binding.rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)

        refChat.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                snapshot.children.forEach {
                    list.add(it.getValue(Message::class.java)!!)
                }
                list.reverse()
                binding.rv.scrollToPosition(list.size)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnSend.setOnClickListener {
            val chatText = binding.etMessage.text.toString().trim()
            if (chatText.isNotBlank()){
                val key = refChat.push().key

                val current = LocalDateTime.now()

                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val formatted = current.format(formatter)

                val message = Message(key,user?.id,user?.name,chatText,formatted)

                refChat.child(key!!).setValue(message)
                binding.etMessage.text.clear()
            }
        }


        return view
    }
}