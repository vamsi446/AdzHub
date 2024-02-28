package com.example.adapp.view.notification

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adapp.R

import com.example.adapp.model.Advertisement

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class NotificationRecyclerViewAdapter(
    private val values: List<Advertisement>,
    val listener: (Advertisement) -> Unit
) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.title
        holder.contentView.text = "₹ ${item.price}"
        holder.categoryView.text = item.category

        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)
        val categoryView : TextView = view.findViewById(R.id.notifyCategoryTV)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}