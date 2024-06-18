package com.example.youtravel.ui.CardItem

// CardAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.youtravel.R

class CardAdapter(private val cardItemList: List<CardItem>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardItemList[position]

        // Use Glide to load the images from URLs
        Glide.with(holder.itemView.context)
            .load(currentItem.image1URL)
            .placeholder(R.drawable.load)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView1)

        holder.textViewName.text = currentItem.name
        holder.textViewTitle.text = currentItem.title
        holder.textViewSubtitle.text = currentItem.subtitle

        Glide.with(holder.itemView.context)
            .load(currentItem.image2URL)
            .placeholder(R.drawable.load)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView2)
    }

    override fun getItemCount() = cardItemList.size

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView1: ImageView = itemView.findViewById(R.id.image1)
        val textViewName: TextView = itemView.findViewById(R.id.name)
        val textViewTitle: TextView = itemView.findViewById(R.id.title)
        val textViewSubtitle: TextView = itemView.findViewById(R.id.subtitle)
        val imageView2: ImageView = itemView.findViewById(R.id.image2)
        val imageViewLike: ImageView = itemView.findViewById(R.id.like)
        val imageViewShare: ImageView = itemView.findViewById(R.id.share)
    }
}
