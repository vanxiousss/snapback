package com.vanxiousss.snapback

/**
 * Created by van.luong
 * on 22,May,2025
 */
sealed class ZoomState {
    data object Idle : ZoomState()
    data object PointerDown : ZoomState()
    data object Zooming : ZoomState()
}