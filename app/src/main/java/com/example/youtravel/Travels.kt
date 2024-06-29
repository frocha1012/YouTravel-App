package com.example.youtravel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.youtravel.config.Jwt
import com.example.youtravel.model.Travel
import com.example.youtravel.network.RetrofitClient
import com.example.youtravel.recyclerview.TravelAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Travels : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TravelAdapter
    private var travelsList = listOf<Travel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.rvTravels)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TravelAdapter(emptyList())  // Initialize with an empty list
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            fetchTravels()
        }
        fetchTravels()
    }

    private fun fetchTravels() {
        val userId = Jwt().getUserID(requireContext())
        RetrofitClient.instance.getTravelsByUserId(userId).enqueue(object : Callback<List<Travel>> {
            override fun onResponse(call: Call<List<Travel>>, response: Response<List<Travel>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        travelsList = body.reversed()
                        adapter = TravelAdapter(travelsList)
                        recyclerView.adapter = adapter
                    } else {
                        Log.e("FetchTravels", "Received empty travel list")
                        // Handle empty or null body appropriately, maybe show a placeholder or message
                    }
                } else {
                    Log.e("FetchTravels", "Failed to fetch travels: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<List<Travel>>, t: Throwable) {
                Log.e("FetchTravels", "Error fetching travels", t)
            }
        })
    }
}