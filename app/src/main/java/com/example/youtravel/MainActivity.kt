package com.example.youtravel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtravel.ui.CardAdapter
import com.example.youtravel.ui.CardItem

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var cardItemList: MutableList<CardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cardItemList = mutableListOf()
        // Add sample data
        cardItemList.add(CardItem(R.drawable.apple_logo, "Name1", "Title1", "Subtitle1", R.drawable.google_logo))
        cardItemList.add(CardItem(R.drawable.baseline_airplanemode_active_24, "Name2", "Title2", "Subtitle2", R.drawable.ic_home_black_24dp))

        cardAdapter = CardAdapter(cardItemList)
        recyclerView.adapter = cardAdapter

        // Implement infinite scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem == totalItemCount - 1) {
                    // Load more items here
                    loadMoreItems()
                }
            }
        })


    }
    private fun loadMoreItems() {
        // Simulate network request to load more items
        // Add new items to the list
        cardItemList.add(CardItem(R.drawable.google_logo, "Name3", "Title3", "Subtitle3", R.drawable.baseline_airplanemode_active_24))
        cardItemList.add(CardItem(R.drawable.baseline_airplanemode_active_24, "Name4", "Title4", "Subtitle4", R.drawable.google_logo))
        // Notify adapter about data changes
        cardAdapter.notifyDataSetChanged()
    }

}