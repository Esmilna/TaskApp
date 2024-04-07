package com.example.proyectofinal.utils.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.EachCategoryItemBinding
import com.example.proyectofinal.utils.model.CategoryData

class CategoryAdapter (private val list: MutableList<CategoryData>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private  val TAG = "CategoryAdapter"
    private var listener:CategoryAdapterInterface? = null
    fun setListener(listener:CategoryAdapterInterface){
        this.listener = listener
    }
    class CategoryViewHolder(val binding: EachCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            EachCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.categoryCategory.text = this.category

                Log.d(TAG, "onBindViewHolder: "+this)
                binding.editCategory.setOnClickListener {
                    listener?.onEditItemClicked(this , position)
                }

                binding.deleteCategory.setOnClickListener {
                    listener?.onDeleteItemClicked(this , position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CategoryAdapterInterface{
        fun onDeleteItemClicked(toDoData: CategoryData , position : Int)
        fun onEditItemClicked(toDoData: CategoryData , position: Int)
    }

}