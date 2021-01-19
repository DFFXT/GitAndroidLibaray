package com.fxffxt.gitandroidlibaray.draw

import android.graphics.Canvas
import android.view.MotionEvent

abstract class ITouchDraw {
    var mutable = false
    abstract fun onTouch(e: MotionEvent)
    abstract fun draw(canvas: Canvas)
    abstract fun pointIn(x:Float,y:Float):Boolean
}