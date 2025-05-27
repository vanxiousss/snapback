package com.vanluong.snapback

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ablanco.zoomy.Zoomy

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val mZoomableView = findViewById<SnapbackImageView>(R.id.test)
//        var zoomListener = ZoomListener(this, mZoomableView, window.decorView as ViewGroup)
//        mZoomableView.setOnTouchListener(zoomListener)

        mZoomableView.setOnClickListener {
            Log.d("Main", "onCreate: Eyyo")
        }
    }
}