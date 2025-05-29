package com.vanxiousss.snapback.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Created by van.luong
 * on 29,May,2025
 */
fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}