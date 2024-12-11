package com.example.capstone.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.ItemIngredientsBinding

class IngredientsAdapter(
    private val ingredients: List<String>,
    private val categories: List<String>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: String, category: String) {
            binding.tvItemName.text = ingredient
            binding.tvItemDescription.text = ""
            binding.tvCategories.text = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position], categories[position])
    }

    override fun getItemCount(): Int = ingredients.size
}

