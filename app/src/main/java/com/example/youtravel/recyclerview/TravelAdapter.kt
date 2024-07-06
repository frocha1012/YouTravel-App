package com.example.youtravel.recyclerview

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtravel.R
import com.example.youtravel.TravelDetails
import com.example.youtravel.model.Travel
import com.example.youtravel.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelAdapter(private val travels: List<Travel>) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class TravelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textTitle)
        val textViewDescription: TextView = view.findViewById(R.id.description_text)
        val imageViewPhoto: ImageView = view.findViewById(R.id.imagePhoto)
        val textViewRating: RatingBar = view.findViewById(R.id.rating_bar)
        val viewButton: View = view.findViewById(R.id.buttonViewTravel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.travel_item, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travels[position]
        holder.textViewTitle.text = travel.title
        holder.textViewDescription.text = travel.description
        holder.textViewRating.rating = travel.rating

        loadTravelImage(travel.id.toString(), holder.imageViewPhoto)

        holder.viewButton.setOnClickListener {
            val context = holder.viewButton.context
            val intent = Intent(context, TravelDetails::class.java).apply {
                putExtra("travelTitle", travel.title)  // Pass the travel title to the TravelDetails activity
            }
            context.startActivity(intent)
        }
    }

    private fun loadTravelImage(travelId: String, imageView: ImageView) {
        RetrofitClient.travelService.getTravelImage(travelId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageView.setImageBitmap(bitmap)
                        inputStream.close()
                    }
                } else {
                    Log.e("API Error", "Failed to fetch image")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API Error", "Error fetching image", t)
            }
        })
    }

    override fun getItemCount() = travels.size
}