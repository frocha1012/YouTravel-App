package com.example.youtravel.ui

// CardAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.youtravel.R

class CardAdapter(private val cardItemList: List<CardItem>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardItemList[position]

        holder.imageView1.setImageResource(currentItem.image1ResId)
        holder.textViewName.text = currentItem.name
        holder.textViewTitle.text = currentItem.title
        holder.textViewSubtitle.text = currentItem.subtitle
        holder.imageView2.setImageResource(currentItem.image2ResId)
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
