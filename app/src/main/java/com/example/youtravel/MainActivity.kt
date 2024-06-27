package com.example.youtravel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtravel.ui.CardItem.CardAdapter
import com.example.youtravel.ui.CardItem.CardItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private var cardItemList: MutableList<CardItem> = mutableListOf()

    // Carregar conteudo no startup.
    // Podemos carregar tudo de uma vez, no entanto, se a lista for muito grande talvez
    // queremos fazer o caching no loadcontent_more.
    fun loadContent_inital() {
        val imagem1 = "http://curiosamente.diariodepernambuco.com.br/wp-content/uploads/2015/11/sol-vermelho1.jpg"
        val imagem2 = "http://pngimg.com/uploads/pokemon/pokemon_PNG2.png"
        val imagem3 = "http://pngimg.com/uploads/pokemon/pokemon_PNG3.png"
        val imagem4 = "http://pngimg.com/uploads/pokemon/pokemon_PNG4.png"

        // Add sample data
        cardItemList.add(CardItem(imagem1, "Name1", "Title1", "Subtitle1", imagem1))
        cardItemList.add(CardItem(imagem3, "Name2", "Title2", "Subtitle2", imagem4))

        val imagem = "http://pngimg.com/uploads/pokemon/pokemon_PNG11.png"
        cardItemList.add(CardItem(imagem, "Name3", "Title3", "Subtitle3", imagem))
        cardItemList.add(CardItem(imagem, "Name4", "Title4", "Subtitle4", imagem))


    }

    // Em principio pode ficar vazia a funcao.
    // Sera usada em situacoes em que a lista e muito grande para adicionar mais dados.
    fun loadContent_more() {

    }

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
        loadContent_inital()
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
                    loadContent_more()
                    cardAdapter.notifyDataSetChanged()
                }
            }
        })

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(this, Camera::class.java))

                    true
                }
                R.id.navigation_place -> {
                    startActivity(Intent(this, MapsActivity::class.java))

                    true
                }
                R.id.navigation_personal_area -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    
}