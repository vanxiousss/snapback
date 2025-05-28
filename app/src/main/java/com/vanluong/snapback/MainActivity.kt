package com.vanluong.snapback

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                Log.d("Main", "onSingleTapConfirmed: Single Tap Detected")
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                Log.d("Main", "onDoubleTap: Double Tap Detected")
                return true
            }
        })

        val mZoomableView = findViewById<SnapbackImageView>(R.id.test)

        mZoomableView.setOnTouchListener { v, event ->
            if (gestureDetector.onTouchEvent(event)) {
                return@setOnTouchListener true
            }
            false
        }

        mZoomableView.setOnClickListener {
            Log.d("Main", "onCreate: Eyyo")
        }

        mZoomableView.setOnLongClickListener {
            Log.d("Main", "onCreate: Long Clicked")
            true
        }
    }
}