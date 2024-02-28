package com.example.adapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adapp.R

class AllAdsCategoryAdapter(val data: Array<String>, val listener: (String) -> Unit) : RecyclerView.Adapter<AllAdsCategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val categoryT = view.findViewById<TextView>(R.id.categoryTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item_category, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = data[position]
        holder.categoryT.text = cat
        holder.itemView.setOnClickListener {
            listener(cat)
        }
    }

    override fun getItemCount() = data.size
}