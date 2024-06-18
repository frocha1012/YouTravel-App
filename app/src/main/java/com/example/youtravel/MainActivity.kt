package com.example.youtravel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtravel.ui.CardItem.CardAdapter
import com.example.youtravel.ui.CardItem.CardItem

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
        val imagem1 = "http://curiosamente.diariodepernambuco.com.br/wp-content/uploads/2015/11/sol-vermelho1.jpg"
        val imagem2 = "http://pngimg.com/uploads/pokemon/pokemon_PNG2.png"
        val imagem3 = "http://pngimg.com/uploads/pokemon/pokemon_PNG3.png"
        val imagem4 = "http://pngimg.com/uploads/pokemon/pokemon_PNG4.png"

        // Add sample data
        cardItemList.add(CardItem(imagem1, "Name1", "Title1", "Subtitle1", imagem1))
        cardItemList.add(CardItem(imagem3, "Name2", "Title2", "Subtitle2", imagem4))

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
        val imagem = "http://pngimg.com/uploads/pokemon/pokemon_PNG11.png"
        cardItemList.add(CardItem(imagem, "Name3", "Title3", "Subtitle3", imagem))
        cardItemList.add(CardItem(imagem, "Name4", "Title4", "Subtitle4", imagem))
        // Notify adapter about data changes
        cardAdapter.notifyDataSetChanged()
    }

}