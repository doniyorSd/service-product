package com.example.sero_service_admin.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.sero_service_admin.R
import com.example.sero_service_admin.adapter.ProductAdapter
import com.example.sero_service_admin.adapter.ProductViewPagerAdapter
import com.example.sero_service_admin.databinding.FragmentAddProductWarehouseBinding
import com.example.sero_service_admin.model.Product
import com.example.sero_service_admin.model.User
import com.example.sero_service_admin.model.UserEnum
import com.example.sero_service_admin.utils.MySharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddProductWarehouseFragment : Fragment() {

    lateinit var myRef: DatabaseReference
    lateinit var refUser: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var adapter: ProductAdapter
    lateinit var list:ArrayList<Product>
    var user:User?= null
    lateinit var binding:FragmentAddProductWarehouseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_product_warehouse, container, false)
         binding = FragmentAddProductWarehouseBinding.bind(view)

        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        val database = FirebaseDatabase.getInstance()
        refUser = database.getReference("users")

        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        myRef = database.getReference("products")



        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                snapshot.children.forEach {
                    list.add(it.getValue(Product::class.java)!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        refUser.child(id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)


                if (user != null) {
                    if (user?.position == UserEnum.USER){
                        binding.btnAdd.visibility = View.GONE
                    }else{
                        binding.btnAdd.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        adapter = ProductAdapter(list,object :ProductAdapter.SellClicks{
            override fun rootClick(product: Product, position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("O'chirish")

                dialog.setPositiveButton("HA"
                ) { p0, p1 ->
                    list.remove(product)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position,list.size)

                    myRef.child(product.id!!).removeValue()
                }
                dialog.setNegativeButton("YO'Q"){p1,p2 ->

                }
                dialog.show()
            }
        })

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), RecyclerView.VERTICAL
        )
        binding.rv.addItemDecoration(dividerItemDecoration)
        binding.rv.adapter = adapter




        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.addNewProductFragment)
        }


        return view
    }

    override fun onResume() {
        super.onResume()

        MySharedPreference.init(requireContext())
        val id = MySharedPreference.id

        refUser.child(id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)


                if (user != null) {
                    if (user?.position == UserEnum.USER){
                        binding.btnAdd.visibility = View.GONE
                    }else{
                        binding.btnAdd.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}