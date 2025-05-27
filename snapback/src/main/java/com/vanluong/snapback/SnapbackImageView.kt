package com.vanluong.snapback

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min


/**
 * Created by van.luong
 * on 21,May,2025
 */
class SnapbackImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr),
    ScaleGestureDetector.OnScaleGestureListener {
    private var shadow: View? = null
    private var zoomableImageView: ImageView? = null
    private val decorView: ViewGroup by lazy {
        (context as Activity).window.decorView as ViewGroup
    }

    private var mCurrentMovementMidPoint = PointF()
    private var mInitialPinchMidPoint = PointF()
    private var mTargetViewCords = Point()

    private val scaleGestureDetector: ScaleGestureDetector by lazy {
        ScaleGestureDetector(context, this)
    }

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                performClick()
                return true
            }
        })
    }

    private var scaleFactor = 1f
    private var state: ZoomState = ZoomState.Idle

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        val scaleHandled = scaleGestureDetector.onTouchEvent(event)
        val gestureHandled = gestureDetector.onTouchEvent(event)

        val finalHandle = scaleHandled || gestureHandled

        val action: Int = event.action and MotionEvent.ACTION_MASK

        when (action) {
            ACTION_POINTER_DOWN, ACTION_DOWN -> {
                when (state) {
                    ZoomState.Idle -> {
                        state = ZoomState.PointerDown
                    }

                    ZoomState.PointerDown -> {
                        state = ZoomState.Zooming
                        midPointOfEvent(mInitialPinchMidPoint, event)
                        showZoomOverlay()
                    }

                    else -> {
                        // Do nothing, we are already zooming or pointer down
                    }
                }
            }

            ACTION_MOVE -> {
                if (state == ZoomState.Zooming) {
                    val focusX = scaleGestureDetector.focusX
                    val focusY = scaleGestureDetector.focusY

                    val dx = focusX - mInitialPinchMidPoint.x
                    val dy = focusY - mInitialPinchMidPoint.y

                    zoomableImageView?.x = mTargetViewCords.x + dx
                    zoomableImageView?.y = mTargetViewCords.y + dy
                }
            }

            ACTION_UP, ACTION_POINTER_UP, ACTION_CANCEL -> {
                when (state) {
                    ZoomState.Zooming -> {
                        endZoomOverlay()
                        state = ZoomState.Idle
                    }

                    ZoomState.PointerDown -> {
                        state = ZoomState.Idle
                    }

                    else -> {
                        // Do nothing, we are already idle or zooming
                    }
                }
            }
        }

        return finalHandle
    }

    private fun addToDecorView(v: View) {
        decorView.addView(v)
    }

    private fun removeFromDecorView(v: View) {
        decorView.removeView(v)
    }

    private fun showZoomOverlay() {
        zoomableImageView = ImageView(context).apply {
            setImageDrawable(this@SnapbackImageView.drawable)
            this.layoutParams =
                ViewGroup.LayoutParams(this@SnapbackImageView.width, this@SnapbackImageView.height)
        }

        mTargetViewCords = getViewAbsoluteCords(this)
        zoomableImageView!!.pivotX = mInitialPinchMidPoint.x
        zoomableImageView!!.pivotY = mInitialPinchMidPoint.y

        zoomableImageView!!.x = mTargetViewCords.x.toFloat()
        zoomableImageView!!.y = mTargetViewCords.y.toFloat()

        if (shadow == null) shadow = View(context).apply {
            this.layoutParams = ViewGroup.LayoutParams(
                decorView.width,
                decorView.height
            )
        }
        shadow!!.setBackgroundResource(0)

        disableParentTouch(parent)
        visibility = INVISIBLE

        addToDecorView(shadow!!)
        addToDecorView(zoomableImageView!!)
    }

    private fun endZoomOverlay() {
        removeFromDecorView(zoomableImageView!!)
        removeFromDecorView(shadow!!)
        visibility = VISIBLE

        zoomableImageView = null
        mCurrentMovementMidPoint = PointF()
        mInitialPinchMidPoint = PointF()
        state = ZoomState.Idle
    }

    companion object {
        private const val MIN_SCALE_FACTOR = 1f
        private const val MAX_SCALE_FACTOR = 5f

        fun midPointOfEvent(point: PointF, event: MotionEvent) {
            if (event.pointerCount == 2) {
                val x = event.getX(0) + event.getX(1)
                val y = event.getY(0) + event.getY(1)
                point[x / 2] = y / 2
            }
        }

        fun getViewAbsoluteCords(v: View): Point {
            val location = IntArray(2)
            v.getLocationInWindow(location)
            val x = location[0]
            val y = location[1]

            return Point(x, y)
        }
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        Log.d("SNAPBACK", "onScale: ")

        if (zoomableImageView == null) {
            return false
        }

        // Update the scale factor
        scaleFactor *= detector.scaleFactor
        scaleFactor = max(MIN_SCALE_FACTOR, min(scaleFactor, MAX_SCALE_FACTOR))

        zoomableImageView!!.apply {
            this.scaleX = scaleFactor
            this.scaleY = scaleFactor
        }

        obscureDecorView(scaleFactor)

        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return zoomableImageView != null
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        scaleFactor = 1f
    }

    private fun obscureDecorView(factor: Float) {
        var normalizedValue =
            (factor - MIN_SCALE_FACTOR) / (MAX_SCALE_FACTOR - MIN_SCALE_FACTOR)
        normalizedValue = min(0.75, (normalizedValue * 2).toDouble()).toFloat()
        val obscure = Color.argb((normalizedValue * 255).toInt(), 0, 0, 0)
        shadow?.setBackgroundColor(obscure)
    }

    private fun disableParentTouch(view: ViewParent) {
        view.requestDisallowInterceptTouchEvent(true)
        if (view.parent != null) {
            disableParentTouch(view.parent)
        }
    }
}