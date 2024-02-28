package com.example.adapp.view.my_ads

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.adapp.R

import com.example.adapp.model.Advertisement

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyAdsRecyclerViewAdapter(
    private val values: List<Advertisement>,
    val listener: (Advertisement) -> Unit
) : RecyclerView.Adapter<MyAdsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_ads, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.adTitle.setText(item.title)
        holder.adDescription.setText(item.description)
        holder.adPrice.text = ("₹ ${item.price.toString()}")
        holder.adLocation.text = (item.location)
        holder.adContactTV.text = (item.contact)

        Glide.with(holder.itemView.context).load(Uri.parse(item.imageUrl)).into(holder.adImage)

        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adTitle: TextView = view.findViewById(R.id.adTitleTV)
        val adPrice: TextView = view.findViewById(R.id.adPriceTV)
        val adLocation: TextView = view.findViewById(R.id.adLocationTV)
        val adDescription: TextView = view.findViewById(R.id.adDescriptionTV)
        val adContactTV: TextView = view.findViewById(R.id.adContactTV)
        val adImage: ImageView =view.findViewById(R.id.adImageTV)

    }
}