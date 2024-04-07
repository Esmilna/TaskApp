package com.example.proyectofinal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentHomeBinding
import com.example.proyectofinal.utils.Adapter.CategoryAdapter
import com.example.proyectofinal.utils.model.CategoryData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(), CategoryDialogFragment.OnCategoryNextBtnClickListener,
    CategoryAdapter.CategoryAdapterInterface {
    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private var frag: CategoryDialogFragment? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryItemList: MutableList<CategoryData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        //get  a data from firebase
        getCategoryFromFirebase()


        binding.addCategoryBtn.setOnClickListener {

            if (frag != null)
                childFragmentManager.beginTransaction().remove(frag!!).commit()
            frag = CategoryDialogFragment()
            frag!!.setListener(this)

            frag!!.show(
                childFragmentManager,
                CategoryDialogFragment.TAG
            )

        }
    }
    private fun getCategoryFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                categoryItemList.clear()
                for (categorySnapshot in snapshot.children) {
                    val categoryCategory =
                        categorySnapshot.key?.let { CategoryData(it, categorySnapshot.value.toString()) }

                    if (categoryCategory != null) {
                        categoryItemList.add(categoryCategory)
                    }

                }
                Log.d(TAG, "onDataChange: " + categoryItemList)
                categoryAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("Categories")
            .child(authId)


        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)

        categoryItemList = mutableListOf()
        categoryAdapter = CategoryAdapter(categoryItemList)
        categoryAdapter.setListener(this)
        binding.mainRecyclerView.adapter = categoryAdapter
    }

    override fun saveCategory(categoryCategory: String, categoryEdit: TextInputEditText) {

        database
            .push().setValue(categoryCategory)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Category Added Successfully", Toast.LENGTH_SHORT).show()
                    categoryEdit.text = null

                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        frag!!.dismiss()

    }

    override fun updateCategory(categoryData: CategoryData, categoryEdit: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[categoryData.categoryId] = categoryData.category
        database.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            frag!!.dismiss()
        }
    }

    override fun onDeleteItemClicked(categoryData: CategoryData, position: Int) {
        database.child(categoryData.categoryId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditItemClicked(categoryData: CategoryData, position: Int) {
        if (frag != null)
            childFragmentManager.beginTransaction().remove(frag!!).commit()

        frag = CategoryDialogFragment.newInstance(categoryData.categoryId, categoryData.category)
        frag!!.setListener(this)
        frag!!.show(
            childFragmentManager,
            CategoryDialogFragment.TAG
        )
    }



}

