package com.example.adapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.adapp.R

class SplashScreen : AppCompatActivity() {
    private var TIME_OUT : Long = 2500
    lateinit var appLogo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        appLogo = findViewById(R.id.logoIV)
        val tagline = findViewById<TextView>(R.id.taglineTV)
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen)
        val taglineAnim = AnimationUtils.loadAnimation(this, R.anim.tagline_pop)
        tagline.startAnimation(taglineAnim)
        appLogo.startAnimation(logoAnim)
        loadSplashScreen()
    }
    private fun loadSplashScreen(){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },TIME_OUT)
    }
}