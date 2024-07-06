package com.example.youtravel.recyclerview

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.youtravel.R  // Ensure this import points to your R file
import com.example.youtravel.model.Stop


class StopsAdapter(private val stops: List<Stop>) : RecyclerView.Adapter<StopsAdapter.StopViewHolder>() {

    class StopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textTitle)
        val imageViewPhoto: ImageView = view.findViewById(R.id.imagePhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stop_item, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        val stop = stops[position]
        holder.textViewTitle.text = stop.title
        if (stop.imageUri.toString().isNotEmpty()) {
            holder.imageViewPhoto.setImageURI(stop.imageUri)  // Load image from URI
        }
    }

    override fun getItemCount() = stops.size
}
