package com.example.youtravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

class IntroMain : AppCompatActivity() {

    private lateinit var indicatorsContainer: LinearLayout
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlider(
                "Discover",
                "Explore new places, experiences, and adventures. Discover the world through the eyes of your friends and fellow travelers.",
                R.drawable.pic1
            ),
            IntroSlider(
                "Capture & Share",
                "Capture your moments and share them with friends. Let your photos tell your story.",
                R.drawable.pic2
            ),
            IntroSlider(
                "Connect",
                "Connect with nature. Follow friends and like-minded travelers to see their latest journeys.",
                R.drawable.pic4
            ),
            IntroSlider(
                "Get Inspired",
                "Get inspired by the community, find new ideas for your next trip in the mountains, and discover top-rated experiences.",
                R.drawable.pic3
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_main)

        val skipIntroText = findViewById<TextView>(R.id.textSkipIntro)
        skipIntroText.setOnClickListener {
            setIntroViewed()
            navigateToRegister()
        }

        setupViewPagerAndIndicators()
    }

    private fun setupViewPagerAndIndicators() {
        val introSliderViewPager = findViewById<ViewPager2>(R.id.introSliderViewPager)
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicators(0)

        introSliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicators(position)
            }
        })

        findViewById<MaterialButton>(R.id.buttonNext).setOnClickListener {
            if (introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                introSliderViewPager.currentItem += 1
            } else {
                setIntroViewed()
                navigateToRegister()
            }
        }
    }

    private fun setIntroViewed() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("IntroViewed", true)
            apply()
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, Register::class.java))
        finish()
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext).apply {
                setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive))
                this.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicators(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive))
            }
        }
    }
}
