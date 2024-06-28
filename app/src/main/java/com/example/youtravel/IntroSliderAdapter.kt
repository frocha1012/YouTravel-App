package com.example.youtravel


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IntroSliderAdapter(private val introSlider: List<IntroSlider>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return IntroSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return introSlider.size
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlider[position])
    }

    inner class IntroSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle = view.findViewById<TextView>(R.id.textTitle)
        private val textDescription = view.findViewById<TextView>(R.id.textDescription)
        private val icon = view.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(introSlider: IntroSlider) {
            textTitle.text = introSlider.title
            textDescription.text = introSlider.description
            icon.setImageResource(introSlider.icon)
        }
    }
}