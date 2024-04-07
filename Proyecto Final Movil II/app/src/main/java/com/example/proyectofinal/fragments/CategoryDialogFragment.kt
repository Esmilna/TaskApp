package com.example.proyectofinal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentCategoryDialogBinding
import com.example.proyectofinal.utils.model.CategoryData
import com.google.android.material.textfield.TextInputEditText


class CategoryDialogFragment : Fragment() {

    private lateinit var binding: FragmentCategoryDialogBinding
    private var listener : OnCategoryNextBtnClickListener? = null
    private var CategoryData: CategoryData? = null


    fun setListener(listener: OnCategoryNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(categoryId: String, category: String) =
            CategoryDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("categoryId", categoryId)
                    putString("category", category)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){

            CategoryData = CategoryData(arguments?.getString("categoryId").toString() ,arguments?.getString("category").toString())
            binding.CategoryEt.setText(CategoryData?.category)
        }


        binding.CategoryClose.setOnClickListener {
            dismiss()
        }

        binding.CategoryNextBtn.setOnClickListener {

            val categoryCategory = binding.CategoryEt.text.toString()
            if (categoryCategory.isNotEmpty()){
                if (CategoryData == null){
                    listener?.saveCategory(categoryCategory , binding.CategoryEt)
                }else{
                    CategoryData!!.category = categoryCategory
                    listener?.updateCategory(CategoryData!!, binding.CategoryEt)
                }

            }
        }
    }



    interface OnCategoryNextBtnClickListener{
        fun saveCategory(categoryCategory:String , categoryEdit: TextInputEditText)
        fun updateCategory(categoryData: CategoryData , categoryEdit: TextInputEditText)
    }


}